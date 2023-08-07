import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}orders`)
  }

  searchOrders(searchProducer: any, page: number, pageSize: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}orders/search?page=${page}&pageSize=${pageSize}`, searchProducer);
  }

  getListOrderItemByOrderId(id): Observable<any> {
    return this.httpClient.get<any>(`${this.API}order-items/${id}`);
  }

  findById(id): Observable<any> {
    return this.httpClient.get<any>(`${this.API}orders/${id}`)
  }

  changeStatusOrder(orders: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}orders/update-status`, orders);
  }
}
