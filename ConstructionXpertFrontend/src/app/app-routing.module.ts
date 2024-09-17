import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthComponent } from './features/auth/auth.component';
import { RegisterComponent } from './features/register/register.component';
import { UserListComponent } from './features/user-list/user-list.component';
import { AdminDashboardComponent } from './features/admin-dashboard/admin-dashboard.component';
import { Role } from './core/enums/Role';
import {authGuard} from "./core/guards/auth.guard";
import {DashboardComponent} from "./features/dashboard/dashboard.component";



const routes: Routes = [
  { path: '', redirectTo: 'login' , pathMatch: 'full' },
  { path: 'login', component: AuthComponent },
  { path: 'dashboard', canActivate: [authGuard], component: DashboardComponent },

  {
    path: 'dashAdmin',
    component: AdminDashboardComponent,
    canActivate: [authGuard],
    data: { expectedRole: Role.ADMIN },
    children: [
      {
        path: 'users',
        component: UserListComponent,
        canActivate: [authGuard],
        data: { expectedRole: Role.ADMIN }
      },
      { path: 'users/register', component: RegisterComponent,
        canActivate: [authGuard],
        data: { expectedRole: Role.ADMIN }},
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
