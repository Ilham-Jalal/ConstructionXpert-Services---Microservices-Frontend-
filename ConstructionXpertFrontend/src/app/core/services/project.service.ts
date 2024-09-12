import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProjectDto } from '../dtos/ProjectDto';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:9191/PROJECT-SERVICE/api/project';

  constructor(private http: HttpClient) {}

  getAllProjects(): Observable<ProjectDto[]> {
    return this.http.get<ProjectDto[]>(`${this.apiUrl}/get-all-projects`);
  }

  getProjectById(id: string): Observable<ProjectDto> {
    return this.http.get<ProjectDto>(`${this.apiUrl}/get-project-by-id/${id}`);
  }

  createProject(projectDto: ProjectDto): Observable<ProjectDto> {
    return this.http.post<ProjectDto>(`${this.apiUrl}/create-project`, projectDto);
  }

  updateProject(id: string, projectDto: ProjectDto): Observable<ProjectDto> {
    return this.http.put<ProjectDto>(`${this.apiUrl}/update-project/${id}`, projectDto);
  }

  deleteProject(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete-project/${id}`);
  }
}
