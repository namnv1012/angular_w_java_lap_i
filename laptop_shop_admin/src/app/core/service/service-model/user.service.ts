import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  search(searchUser: any, page: any, pageSize: any) {
    return this.httpClient.post<any>(`${this.API}user/search?page=${page}&page-size=${pageSize}`, searchUser);
  }

  export() {
    return this.httpClient.post(`${this.API}user/export`, {responseType: 'blob'});
  }

  searchCustomer(searchUser: any, page: any, pageSize: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}account/get-list-customer?page=${page}&page-size=${pageSize}`, searchUser)
  }
}
