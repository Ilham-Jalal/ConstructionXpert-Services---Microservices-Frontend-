import {Role} from "../enums/Role";

export interface UserDto {
  id: string;
  username: string;
  password: string;
  email: string;
  role: Role;
  profilePicture: string;
}
