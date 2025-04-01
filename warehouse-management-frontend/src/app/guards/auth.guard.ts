import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.authService.verifyToken().pipe(
      tap(isValid => {
        if (!isValid) {
          console.warn('Not logged in, redirecting to login');
          this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        }
      })
    );
  }
}
