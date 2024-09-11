import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AdminDto} from "../dto/AdminDto";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getAllAdmins(): Observable<AdminDto[]> {
    return this.http.get<AdminDto[]>(`${this.apiUrl}/get-all-admins`);
  }

  getAdminById(id: string): Observable<AdminDto> {
    return this.http.get<AdminDto>(`${this.apiUrl}/get-admin-by-id/${id}`);
  }

  updateAdmin(id: string, adminDto: AdminDto): Observable<AdminDto> {
    return this.http.put<AdminDto>(`${this.apiUrl}/update-admin/${id}`, adminDto);
  }

  deleteAdmin(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete-admin/${id}`);
  }
}
