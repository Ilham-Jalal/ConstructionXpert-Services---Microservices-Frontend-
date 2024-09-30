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
  geolocation: string | null = null;
  status: string | null = null;
  minBudget: number | null = null;
  maxBudget: number | null = null;
  dateStart: string | null = null;
  dateEnd: string | null = null;

  isFiltering: boolean = false;

  constructor(private projectService: ProjectService) {}
  ngOnInit(): void {
    this.getProjects();
  }

  // Get all projects without filters
  getProjects(): void {
    if (!this.isFiltering) {
      this.projectService.getAllProjects(this.page, this.size, this.sortField, this.sortDirection).subscribe(
        (response: any) => {
          this.projects = response.content;
          this.totalProjects = response.totalElements;
        },
        (error) => {
          console.error('Error fetching projects', error);
        }
      );
    } else {
      this.filterProjects();
    }
  }

  // Dynamic filtering method
  filterProjects(): void {
    this.projectService.dynamicFilterProjects(
      this.geolocation, 
      this.status, 
      this.minBudget, 
      this.maxBudget, 
      this.dateStart, 
      this.dateEnd, 
      this.page, 
      this.size, 
      this.sortField, 
      this.sortDirection
    ).subscribe(
      (response: any) => {
        this.projects = response.content;
        this.totalProjects = response.totalElements;
      },
      (error) => {
        console.error('Error fetching filtered projects', error);
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

  applyFilters(): void {
    this.isFiltering = true;
    this.filterProjects();
  }

  onSortChange(sortField: string, sortDirection: string): void {
    this.sortField = sortField;
    this.sortDirection = sortDirection;
    this.getProjects();
  }
  

  resetFilters(): void {
    this.geolocation = '';
    this.status = '';
    this.minBudget = null;
    this.maxBudget = null;
    this.dateStart = null;
    this.dateEnd = null;
    this.isFiltering = false;
    this.getProjects();
  }
}
