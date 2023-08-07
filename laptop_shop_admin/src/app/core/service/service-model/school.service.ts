import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {SearchSchoolModel} from '../model/search-school.model';

@Injectable({
  providedIn: 'root'
})
export class SchoolService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  search(searchSchool: SearchSchoolModel, page: any, pageSize: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}schools/search?page=${page}&page-size=${pageSize}`, searchSchool);
  }

  export(searchSchool: SearchSchoolModel) {
    return this.httpClient.post(`${this.API}schools/export`, searchSchool, {responseType: 'blob'});
  }
}
