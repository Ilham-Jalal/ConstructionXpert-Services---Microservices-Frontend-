import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProjectDto } from '../dtos/ProjectDto';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = `${environment.apiProject}api/project`;

  constructor(private http: HttpClient) {}

  getAllProjects(page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);

    return this.http.get(`${this.apiUrl}/get-all-projects`, { params });
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
