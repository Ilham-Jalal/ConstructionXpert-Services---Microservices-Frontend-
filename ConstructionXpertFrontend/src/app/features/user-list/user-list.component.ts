import { Component, OnInit } from '@angular/core';
import { User } from '../../core/models/User';
import { UserService } from '../../core/services/user.service';
import { Role } from '../../core/enums/Role';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  selectedUser: User | null = null;
  searchUsername: string = '';
  errorMessage: string = '';
  roles: Role[] = [Role.ADMIN, Role.CLIENT, Role.SUPERVISOR];  // Les rôles disponibles

  constructor(private userService: UserService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.getAllUsers();
  }

  getRoleClass(role: Role): string {
    return `role-${role.toLowerCase()}`;
  }

  getAllUsers(): void {
    this.userService.getAllUsers().subscribe(
      (data: User[]) => {
        this.users = data;
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des utilisateurs.';
        console.error(error);
      }
    );
  }

  getUserByUsername(): void {
    if (this.searchUsername) {
      this.userService.getUserByUsername(this.searchUsername).subscribe(
        (data: User) => {
          this.users = [data];
          this.errorMessage = '';
        },
        (error) => {
          this.errorMessage = 'Utilisateur non trouvé.';
          console.error(error);
        }
      );
    }
  }

  editUser(user: User): void {
    this.selectedUser = { ...user };
  }


  cancelUpdate(): void {
    this.selectedUser = null;
  }

  updateUser(): void {
    if (this.selectedUser) {
      this.userService.updateUser(this.selectedUser.id, this.selectedUser).subscribe(
        (updatedUser: User) => {
          this.snackBar.open('Utilisateur mis à jour avec succès', 'Fermer', { duration: 3000 });
          this.getAllUsers();
          this.selectedUser = null;
        },
        (error) => {
          console.error(error);
          this.snackBar.open('Erreur lors de la mise à jour de l\'utilisateur', 'Fermer', { duration: 3000 });
        }
      );
    }
  }


  deleteUser(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      this.userService.deleteUser(id).subscribe(
        () => {
          this.snackBar.open('Utilisateur supprimé avec succès', 'Fermer', { duration: 3000 });
          this.getAllUsers();
        },
        (error) => {
          console.error(error);
          this.snackBar.open('Erreur lors de la suppression de l\'utilisateur', 'Fermer', { duration: 3000 });
        }
      );
    }
  }
}
