import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule }   from '@angular/router';

// import { HomeComponent } from './components/home/home.component';
// import { NotFoundComponent } from './components/not-found/not-found.component';
import { LoginComponent } from './component/login/login.component';
import { DeviceListComponent } from './component/device-list/device-list.component';
import { AddRangeComponent } from './component/add-range/add-range.component';

const appRoutes: Routes = [
//   { path: '', pathMatch: 'full', component: HomeComponent },
//   { path: 'not-found', component: NotFoundComponent },
  
  { path: 'login', component: LoginComponent},
  { path: 'devices', component: DeviceListComponent, pathMatch: 'full' },
  { path: 'add_range', component: AddRangeComponent, pathMatch: 'full' },
  {path: '', component: LoginComponent},
  { path: '**', redirectTo: 'not-found' }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
