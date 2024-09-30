import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { debounceTime, switchMap } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { User } from '../../../../core/models/User';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/ngrx/app.state';
import { selectUser } from '../../../../core/ngrx/auth.selectors';
import { ProjectService } from '../../../../core/services/project.service';  // Replace with your actual service

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  user$: Observable<User | null>;
  searchControl = new FormControl();
  filteredOptions: Observable<string[]>;

  constructor(private store: Store<AppState>, private projectService: ProjectService) {
    this.user$ = this.store.select(selectUser);
    this.filteredOptions = this.searchControl.valueChanges.pipe(
      debounceTime(300),
      switchMap(value => this.projectService.autocompleteSearch(value || ''))
    );
  }

  ngOnInit() {
    this.filteredOptions = this.searchControl.valueChanges.pipe(
      debounceTime(300),
      switchMap(value => this.projectService.autocompleteSearch(value || ''))
    );
  }
}
