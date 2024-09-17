import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {MatButton} from "@angular/material/button";
import {NgClass} from "@angular/common";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  standalone: true,
  imports: [
    RouterOutlet,
    MatButton,
    NgClass,
    RouterLink,
    MatIcon
  ],
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  currentRoute$: Observable<string>;

  constructor(private router: Router) {
    this.currentRoute$ = this.router.events.pipe(
      map(() => this.router.url)
    );
  }

  isActive(route: string): Observable<boolean> {
    return this.currentRoute$.pipe(
      map(currentRoute => currentRoute === route)
    );
  }

  logoutit() {
    // Assurez-vous que vous avez un service d'authentification injecté ici pour gérer la déconnexion
    // this.authService.logout();
    this.router.navigate(['/']);
  }
}
