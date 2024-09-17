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
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RegisterComponent } from './features/register/register.component';
import { AdminDashboardComponent } from './features/admin-dashboard/admin-dashboard.component';
import { UserListComponent } from './features/user-list/user-list.component';
import {MatOption, MatSelect} from "@angular/material/select";
import {MatSidenav, MatSidenavContainer} from "@angular/material/sidenav";
import {MatToolbar} from "@angular/material/toolbar";
import {MatListItem, MatNavList} from "@angular/material/list";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {MatCard, MatCardHeader} from "@angular/material/card";
import {NgOptimizedImage} from "@angular/common";
import { ResourceListComponent } from './features/resource-list/resource-list.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    PasswordStrengthPipe,
    RegisterComponent,
    UserListComponent,
    ResourceListComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    StoreModule.forRoot(reducers, {metaReducers}),
    BrowserAnimationsModule, // Remplacement par BrowserAnimationsModule pour animations
    // Angular Material modules
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
    FormsModule,
    MatSelect,
    MatOption,
    MatSidenavContainer,
    MatSidenav,
    MatToolbar,
    MatNavList,
    MatListItem,
    MatMenuTrigger,
    MatMenu,
    MatMenuItem,
    MatGridList,
    MatGridTile,
    MatCard,
    MatCardHeader,
    NgOptimizedImage,
    AdminDashboardComponent,
    AdminDashboardComponent
  ],
  providers: [
    provideHttpClient(withInterceptors([authInterceptor]), withFetch()) // Utilisation d'HttpClient avec intercepteurs
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
