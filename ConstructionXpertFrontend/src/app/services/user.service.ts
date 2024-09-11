import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../models/User";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/get-all-users`);
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/get-user-by-id/${id}`);
  }

  getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/get-user-by-username/${username}`);
  }
}
