import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../../../core/models/User';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/ngrx/app.state';
import { selectUser } from '../../../../core/ngrx/auth.selectors';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  user$: Observable<User | null>;

  constructor(private store: Store<AppState>){
    this.user$ = this.store.select(selectUser)
  }

}
