import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WarrantyService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}warranties`)
  }

  addProducer(producer: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}warranties`, producer);
  }

  searchProducer(searchProducer: any, page: number, pageSize: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}warranties/search?page=${page}&pageSize=${pageSize}`, searchProducer);
  }

  getListProductById(id: number): Observable<any> {
    return this.httpClient.get(`${this.API}warranties/get-list-product/${id}`);
  }
}
