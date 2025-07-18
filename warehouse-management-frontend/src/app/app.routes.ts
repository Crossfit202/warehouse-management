import { Routes } from '@angular/router';
import { WarehousesComponent } from './components/warehouses/warehouses.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { UsersComponent } from './components/users/users.component';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AdminComponent } from './components/admin/admin.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthGuard } from './guards/auth.guard';
import { RegisterComponent } from './components/register/register.component';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';
import { InventoryComponent } from './components/inventory/inventory.component';
import { StorageLocationsComponent } from './components/storage-locations/storage-locations.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { MovementsComponent } from './components/movements/movements.component';
import { ProductsComponent } from './components/products/products.component';

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
        path: 'register',
        component: RegisterComponent
    },
    {
        path: 'unauthorized',
        component: UnauthorizedComponent
    },


    // Protected Routes
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER'] }
    },
    {
        path: 'warehouses',
        component: WarehousesComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER', 'INV_CLERK'] }
    },
    {
        path: 'storage-locations',
        component: StorageLocationsComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER', 'INV_CLERK'] }
    },
    {
        path: 'alerts',
        component: AlertsComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER', 'INV_CLERK'] }
    },
    {
        path: 'admin',
        component: AdminComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN'] }
    },
    {
        path: 'storage-locations',
        component: StorageLocationsComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER', 'INV_CLERK'] }
    },
    {
        path: 'inventory',
        component: InventoryComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER', 'INV_CLERK'] }
    },
    {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [AuthGuard],
        data: { role: ['USER', 'ADMIN', 'MANAGER', 'INV_CLERK'] }
    },
    {
        path: 'users',
        component: UsersComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER'] }
    },
    {
        path: 'movements',
        component: MovementsComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER', 'INV_CLERK'] }
    },

    {
        path: 'products',
        component: ProductsComponent,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN', 'MANAGER', 'INV_CLERK'] }
    },

    // Fallback Route
    {
        path: '**',
        redirectTo: ''
    }
];
