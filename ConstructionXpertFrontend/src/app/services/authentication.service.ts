import { Injectable } from '@angular/core';
import {AuthenticationRequest} from "../dto/AuthenticationRequest";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AuthenticationResponse} from "../dto/AuthenticationResponse";
import {AdminDto} from "../dto/AdminDto";
import {ClientDto} from "../dto/ClientDto";
import {SupervisorDto} from "../dto/SupervisorDto";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private apiUrl = 'http://localhost:9191/api/auth';

  constructor(private http: HttpClient) {}

  login(authRequest: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.apiUrl}/login`, authRequest);
  }

  registerAdmin(adminDto: AdminDto): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.apiUrl}/register/admin`, adminDto);
  }

  registerClient(clientDto: ClientDto): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.apiUrl}/register/client`, clientDto);
  }

  registerSupervisor(supervisorDto: SupervisorDto): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.apiUrl}/register/supervisor`, supervisorDto);
  }
}
