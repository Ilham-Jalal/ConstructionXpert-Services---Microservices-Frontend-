import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ProjectService } from '../../../core/services/project.service';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {
  projects: any[] = [];
  totalProjects: number = 0;
  page: number = 0;
  size: number = 10;
  sortField: string = 'name'; 
  sortDirection: string = 'asc';

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.getProjects();
  }

  getProjects(): void {
    this.projectService.getAllProjects(this.page, this.size, this.sortField, this.sortDirection).subscribe(
      (response: any) => {
        this.projects = response.content;
        this.totalProjects = response.totalElements;
      },
      (error) => {
        console.error('Error fetching projects', error);
      }
    );
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.getProjects();
  }

  toggleSortField(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.getProjects();
  }

  getSortIndicator(field: string): string {
    if (this.sortField === field) {
      return this.sortDirection === 'asc' ? '↑' : '↓';
    }
    return '';
  }
}
