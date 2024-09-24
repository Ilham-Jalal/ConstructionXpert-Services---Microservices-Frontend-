import { Component } from '@angular/core';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent {

  total_project = 5;
  projects = [
    {
      id: 1,
      name: 'Project Alpha',
      budget: 10000,
      progress: 30,
      status: 'IN_PROGRESS',
      areaSize: 150,
      geolocation: 'New York, USA',
      room: 4,
      picture: 'path_to_image_alpha'
    },
    {
      id: 2,
      name: 'Project Beta',
      budget: 8000,
      progress: 70,
      status: 'TODO',
      areaSize: 120,
      geolocation: 'London, UK',
      room: 3,
      picture: 'path_to_image_beta'
    },
    {
      id: 2,
      name: 'Project Beta',
      budget: 8000,
      progress: 70,
      status: 'TODO',
      areaSize: 120,
      geolocation: 'London, UK',
      room: 3,
      picture: 'path_to_image_beta'
    },
    {
      id: 2,
      name: 'Project Beta',
      budget: 8000,
      progress: 70,
      status: 'TODO',
      areaSize: 120,
      geolocation: 'London, UK',
      room: 3,
      picture: 'path_to_image_beta'
    }
  ];

  getProgressColor(status: string): string {
    switch (status) {
      case 'TODO':
        return 'warn';  // Rouge pour TODO
      case 'IN_PROGRESS':
        return 'primary';  // Vert pour IN_PROGRESS
      case 'COMPLETED':
        return 'accent';  // Orange pour COMPLETED
      default:
        return 'primary';
    }
  }

}
