import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { AdminDto } from "../dto/AdminDto";
import { ClientDto } from "../dto/ClientDto";
import { SupervisorDto } from "../dto/SupervisorDto";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthenticationService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      role: ['', Validators.required],
      profilePicture: ['',Validators.required]
    });
  }

  ngOnInit(): void {
    console.log('RegisterComponent initialized');
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    const { username, password, email, role, profilePicture} = this.registerForm.value;
    let dto: AdminDto | ClientDto | SupervisorDto;

    switch (role) {
      case 'ADMIN':
        dto = { username, password, email, role, profilePicture } as AdminDto;
        this.authService.registerAdmin(dto).subscribe({
          next: (response) => {
            console.log('Admin registered:', response);
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error('Registration error:', err);
            this.errorMessage = err.error?.message || 'Registration failed';
          }
        });
        break;

      case 'CLIENT':
        dto = { username, password, email, role, profilePicture } as ClientDto;
        this.authService.registerClient(dto).subscribe({
          next: (response) => {
            console.log('Client registered:', response);
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error('Registration error:', err);
            this.errorMessage = err.error?.message || 'Registration failed';
          }
        });
        break;

      case 'SUPERVISOR':
        dto = { username, password, email, role, profilePicture } as SupervisorDto;
        this.authService.registerSupervisor(dto).subscribe({
          next: (response) => {
            console.log('Supervisor registered:', response);
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error('Registration error:', err);
            this.errorMessage = err.error?.message || 'Registration failed';
          }
        });
        break;

      default:
        this.errorMessage = 'Unknown role';
    }
  }
}
