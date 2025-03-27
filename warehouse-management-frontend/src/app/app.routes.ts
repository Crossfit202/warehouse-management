import { Routes } from '@angular/router';
import { WarehousesComponent } from './components/warehouses/warehouses.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { UsersComponent } from './components/users/users.component';

export const routes: Routes = [
    {
        path: '',
        component: HomepageComponent,
        pathMatch: 'full'
    },

    {
        path: 'warehouses',
        component: WarehousesComponent
    },
    {
        path: 'users',
        component: UsersComponent
    }
];
