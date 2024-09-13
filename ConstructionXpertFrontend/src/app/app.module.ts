import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClient, HttpClientModule, provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth-interceptor';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { AuthComponent } from './features/auth/auth.component';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { PasswordStrengthPipe } from './shared/pipes/password-strength.pipe';
import { StoreModule } from '@ngrx/store';
import { reducers, metaReducers } from './core/ngrx/app.state';
import { ReactiveFormsModule } from '@angular/forms';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { SidebarComponent } from './features/dashboard/components/sidebar/sidebar.component';
import { NavbarComponent } from './features/dashboard/components/navbar/navbar.component';
import { MainComponent } from './features/dashboard/components/main/main.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    PasswordStrengthPipe,
    DashboardComponent,
    SidebarComponent,
    NavbarComponent,
    MainComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
    MatToolbarModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    StoreModule.forRoot(reducers, { metaReducers } )
  ],
  providers: [
    HttpClient,
    provideHttpClient(withInterceptors([authInterceptor]), withFetch()),
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
