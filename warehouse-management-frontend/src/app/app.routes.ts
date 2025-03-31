import { Routes } from '@angular/router';
import { WarehousesComponent } from './components/warehouses/warehouses.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { UsersComponent } from './components/users/users.component';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AdminComponent } from './components/admin/admin.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
    // Public Routes
    {
        path: '',
        component: HomepageComponent,
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'warehouses',
        component: WarehousesComponent
    },
    {
        path: 'users',
        component: UsersComponent
    },

    // Protected Routes
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        data: { role: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER'] }
    },
    {
        path: 'admin',
        component: AdminComponent,
        canActivate: [AuthGuard],
        data: { role: ['ROLE_ADMIN'] }
    },
    {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [AuthGuard],
        data: { role: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER'] }
    },

    // Fallback Route
    {
        path: '**',
        redirectTo: ''
    }
];
