import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin.service';
import { AdminDto } from '../dto/AdminDto';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgIf, NgFor } from '@angular/common';

@Component({
  selector: 'app-admin-management',
  templateUrl: './admin-management.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgFor],
  styleUrls: ['./admin-management.component.css']
})
export class AdminManagementComponent implements OnInit {
  admins: AdminDto[] = [];
  selectedAdmin: AdminDto | null = null;
  adminForm: FormGroup;
  errorMessage: string | null = null;

  constructor(private adminService: AdminService, private fb: FormBuilder) {
    this.adminForm = this.fb.group({
      id: [''],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    this.getAllAdmins();
  }


  getAllAdmins(): void {
    this.adminService.getAllAdmins().subscribe({
      next: (data) => {
        this.admins = data;
        console.log('Admins loaded:', this.admins);
      },
      error: (err) => {
        console.error('Error loading admins:', err);
        this.errorMessage = 'Could not load admins';
      }
    });
  }

  getAdminById(id: string): void {
    this.adminService.getAdminById(id).subscribe({
      next: (admin) => {
        this.selectedAdmin = admin;
        this.adminForm.patchValue(admin);
        console.log('Admin loaded:', admin);
      },
      error: (err) => {
        console.error('Error loading admin:', err);
        this.errorMessage = 'Could not load admin';
      }
    });
  }

  updateAdmin(): void {
    if (this.adminForm.invalid || !this.selectedAdmin) {
      this.errorMessage = 'Form is invalid';
      return;
    }

    const updatedAdmin: AdminDto = this.adminForm.value;
    this.adminService.updateAdmin(this.selectedAdmin.id, updatedAdmin).subscribe({
      next: (admin) => {
        console.log('Admin updated:', admin);
        this.getAllAdmins(); 
      },
      error: (err) => {
        console.error('Error updating admin:', err);
        this.errorMessage = 'Could not update admin';
      }
    });
  }

  deleteAdmin(id: string): void {
    this.adminService.deleteAdmin(id).subscribe({
      next: () => {
        console.log('Admin deleted:', id);
        this.getAllAdmins(); // Refresh admin list
      },
      error: (err) => {
        console.error('Error deleting admin:', err);
        this.errorMessage = 'Could not delete admin';
      }
    });
  }

  onSelectAdmin(admin: AdminDto): void {
    this.selectedAdmin = admin;
    this.adminForm.patchValue(admin);
  }

  resetForm(): void {
    this.selectedAdmin = null;
    this.adminForm.reset();
  }
}
