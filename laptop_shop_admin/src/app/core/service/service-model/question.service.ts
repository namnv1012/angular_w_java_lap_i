import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}questions`)
  }

  add(subject: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}questions`, subject);
  }

  export(classroomSearch: any) {
    return this.httpClient.post(`${this.API}questions/export`, classroomSearch, {responseType: 'blob'});
  }

  search(searchSubject: any, page: number, pageSize: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}questions/search?page=${page}&pageSize=${pageSize}`, searchSubject);
  }

  delete(subject: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}questions/delete`, subject);
  }

}
