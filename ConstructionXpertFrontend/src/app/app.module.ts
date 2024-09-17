import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth-interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthComponent } from './features/auth/auth.component';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { PasswordStrengthPipe } from './shared/pipes/password-strength.pipe';
import { StoreModule } from '@ngrx/store';
import { reducers, metaReducers } from './core/ngrx/app.state';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './features/register/register.component';
import { AdminDashboardComponent } from './features/admin-dashboard/admin-dashboard.component';
import { UserListComponent } from './features/user-list/user-list.component';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { NgOptimizedImage } from '@angular/common';
import { ResourceListComponent } from './features/resource-list/resource-list.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { SidebarComponent } from './features/dashboard/components/sidebar/sidebar.component';
import { NavbarComponent } from './features/dashboard/components/navbar/navbar.component';
import { MainComponent } from './features/dashboard/components/main/main.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    PasswordStrengthPipe,
    RegisterComponent,
    UserListComponent,
    ResourceListComponent,
    DashboardComponent,
    SidebarComponent,
    NavbarComponent,
    MainComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    StoreModule.forRoot(reducers, {metaReducers}),
    BrowserAnimationsModule,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
    MatToolbarModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
    MatSelectModule,
    MatMenuModule,
    MatGridListModule,
    NgOptimizedImage,
    AdminDashboardComponent
  ],
  providers: [
    provideHttpClient(withInterceptors([authInterceptor]), withFetch()) // Utilisation d'HttpClient avec intercepteurs
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
