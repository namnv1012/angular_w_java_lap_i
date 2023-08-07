import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {CommonServiceService} from '../utils/common-service.service';

@Injectable({
  providedIn: 'root',
})
export class ClassroomService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient,
              private commonService: CommonServiceService) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}classrooms`)
  }

  export(classroomSearch: any) {
    return this.httpClient.post(`${this.API}classrooms/export`, classroomSearch, {responseType: 'blob'});
  }

  search(classroomSearch: any, page: number, pageSize: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}classrooms/search?page=${page}&pageSize=${pageSize}`, classroomSearch);
  }

  delete(classroom: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}classrooms/delete`, classroom);
  }
}
