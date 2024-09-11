import {Role} from "../enums/Role";

export interface AuthenticationResponse {
  token: string;
  role: Role;
}
