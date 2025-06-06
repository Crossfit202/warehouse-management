import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable, of } from 'rxjs';
import { map, switchMap, take, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  // canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
  //   return this.authService.verifyToken().pipe(
  //     tap(isValid => {
  //       if (!isValid) {
  //         console.warn('Not logged in, redirecting to login');
  //         this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
  //       }
  //     })
  //   );
  // }


  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.authService.verifyToken().pipe(
      switchMap(isValid => {
        if (!isValid) {
          this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
          return of(false);
        }

        const expectedRoles = route.data['role'] as string[] | undefined;

        return this.authService.getUserRole().pipe(
          take(1),
          map(userRole => {
            const role = userRole?.replace(/^ROLE_/, '').trim().toUpperCase(); // Normalize role
            const isAuthorized = expectedRoles?.some(r => r.toUpperCase() === role) ?? true;

            if (!isAuthorized) {
              console.warn(`Access denied for role: ${role}`);
              this.router.navigate(['/unauthorized']);
            }

            return isAuthorized;
          })
        );
      })
    );
  }


}
