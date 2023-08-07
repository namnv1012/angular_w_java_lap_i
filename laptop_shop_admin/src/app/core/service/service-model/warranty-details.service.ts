import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WarrantyDetailsService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}warranty-details`)
  }

  addWarrantyDetails(producer: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}warranty-details`, producer);
  }

  findByWarrantyId(id: number): Observable<any> {
    return this.httpClient.get(`${this.API}warranty-details/findByWarrantyId/${id}`);
  }

}
