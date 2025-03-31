import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(): Observable<boolean> {
    return this.authService.verifyToken().pipe(
      tap(isValid => {
        if (!isValid) {
          console.warn('Token invalid, redirecting to login');
          localStorage.setItem('returnUrl', this.router.url); // Store the return URL
          this.router.navigate(['/login']);
        }
      })
    );
  }
}
