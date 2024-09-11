import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ClientDto} from "../dto/ClientDto";

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private apiUrl = 'http://localhost:8080/api/client';

  constructor(private http: HttpClient) {}

  getAllClients(): Observable<ClientDto[]> {
    return this.http.get<ClientDto[]>(`${this.apiUrl}/get-all-clients`);
  }

  getClientById(id: string): Observable<ClientDto> {
    return this.http.get<ClientDto>(`${this.apiUrl}/get-client-by-id/${id}`);
  }

  updateClient(id: string, clientDto: ClientDto): Observable<ClientDto> {
    return this.http.put<ClientDto>(`${this.apiUrl}/update-client/${id}`, clientDto);
  }

  deleteClient(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete-client/${id}`);
  }
}
