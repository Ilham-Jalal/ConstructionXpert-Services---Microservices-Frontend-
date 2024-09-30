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
<<<<<<< HEAD
  }

  dynamicFilterProjects(
    geolocation?: string | null,
    status?: string | null,
    minBudget?: number | null,
    maxBudget?: number | null,
    dateStart?: string | null,
    dateEnd?: string | null,
    page: number = 0,
    size: number = 10,
    sortField: string = 'name',
    sortDirection: string = 'asc'
  ): Observable<any> {
    let params = new HttpParams();
    if (geolocation) params = params.append('geolocation', geolocation);
    if (status) params = params.append('status', status);
    if (minBudget) params = params.append('minBudget', minBudget.toString());
    if (maxBudget) params = params.append('maxBudget', maxBudget.toString());
    if (dateStart) params = params.append('dateStart', dateStart);
    if (dateEnd) params = params.append('dateEnd', dateEnd);
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    params = params.append('sortField', sortField);
    params = params.append('sortDirection', sortDirection);

    return this.http.get(`${this.apiUrl}/dynamic-filter`, { params });
  }

  autocompleteSearch(input: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/autocomplete-search?input=${input}`);
=======
>>>>>>> d54fe63b31b0357ac39ad3b867dbe4d8e068fc09
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
