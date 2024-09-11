import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIf } from "@angular/common";
import { AuthenticationService } from "../../services/authentication.service";
import { AuthenticationRequest } from "../../dto/AuthenticationRequest";
import { Role } from "../../enums/Role";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],

  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  standalone: true
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    const loginRequest: AuthenticationRequest = this.loginForm.value;

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        const { token, role } = response;
        console.log('Received token:', token);
        console.log('Received role:', role);

        localStorage.setItem('jwt', token);

        // Navigate based on the role
        switch (role) {
          case Role.ADMIN:
            this.router.navigate(['/dashboard/equipments/']);
            break;
          case Role.CLIENT:
          case Role.SUPERVISOR:
            this.router.navigate(['/']);
            break;
          default:
            console.log('Unknown role:', role);
            this.router.navigate(['/access-denied']);
            break;
        }
      },
      error: (err) => {
        console.error('Login error:', err);
        this.errorMessage = 'Invalid username or password';
      }
    });
  }
}
