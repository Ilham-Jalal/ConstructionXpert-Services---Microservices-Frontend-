import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskDto } from '../dtos/TaskDto';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = `${environment.apiTask}api/tasks`;

  constructor(private http: HttpClient) {}

  createTask(taskDto: TaskDto): Observable<TaskDto> {
    return this.http.post<TaskDto>(`${this.apiUrl}/create-task`, taskDto);
  }

  getTaskById(id: number): Observable<TaskDto> {
    return this.http.get<TaskDto>(`${this.apiUrl}/get-task-by-id/${id}`);
  }

  getTasksByProjectId(projectId: number): Observable<TaskDto[]> {
    return this.http.get<TaskDto[]>(`${this.apiUrl}/get-tasks-by-project/${projectId}`);
  }

  getTasksIdsByProjectId(projectId: number): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/get-tasks-ids-by-project/${projectId}`);
  }

  updateTask(id: number, taskDto: TaskDto): Observable<TaskDto> {
    return this.http.put<TaskDto>(`${this.apiUrl}/update-task/${id}`, taskDto);
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete-task/${id}`);
  }
}
