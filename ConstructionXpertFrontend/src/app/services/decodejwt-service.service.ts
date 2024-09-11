import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class DecodejwtService {
  decodeToken(token: string): any {
    console.log("hhhhhhhhhhhh"+token)
    return jwtDecode(token);

  }

  getUsernameFromToken(token: string) {
    const decodedToken = this.decodeToken(token);
    return decodedToken?.sub || null;
  }

  getUserRole(token: string) {
    const decodedToken = this.decodeToken(token);
    return decodedToken?.roles ;
  }
}
