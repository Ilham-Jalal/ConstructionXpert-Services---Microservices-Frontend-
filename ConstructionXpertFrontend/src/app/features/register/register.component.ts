import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {Role} from "../../core/enums/Role";
import {AuthenticationService} from "../../core/services/authentication.service";
import {AuthenticationResponse} from "../../core/dtos/AuthenticationResponse";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  roles = Object.values(Role);
  selectedRole = Role.ADMIN;

  constructor(private fb: FormBuilder, private authService: AuthenticationService,private route:Router) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      profilePicture: ['',[Validators.required]],
      role: [this.selectedRole, Validators.required]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const formData = this.registerForm.value;
      switch (formData.role) {
        case Role.ADMIN:
          this.authService.registerAdmin(formData).subscribe(
            (response: AuthenticationResponse) => this.handleResponse(response),
            (error) => this.handleError(error)
          );
          break;
        case Role.CLIENT:
          this.authService.registerClient(formData).subscribe(
            (response: AuthenticationResponse) => this.handleResponse(response),
            (error) => this.handleError(error)
          );
          break;
        case Role.SUPERVISOR:
          this.authService.registerSupervisor(formData).subscribe(
            (response: AuthenticationResponse) => this.handleResponse(response),
            (error) => this.handleError(error)
          );
          break;
      }
      this.route.navigate(["/dashAdmin/users"])
    }
  }

  private handleResponse(response: AuthenticationResponse) {
    console.log('Enregistrement réussi:', response);
  }

  private handleError(error: any) {
    console.error('Erreur lors de l\'enregistrement:', error);
  }
}
