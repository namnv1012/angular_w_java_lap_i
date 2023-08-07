import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {OrdersModel} from "../model/orders.model";

@Injectable({
  /* cung cấp các dịch vụ có sẵn trên ứng dụng dưới dạng 1 ứng dụng */
  providedIn: 'root'
})
export class CheckoutService {
  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private http: HttpClient) {
  }

  verifi(query: string): Observable<any> {
    const auth = sessionStorage.getItem('token');
    return this.http.post<any>(`${this.API}verify-payment-result?${query}`,
      {}, {headers: new HttpHeaders().set('Authorization', `Bearer ${auth}`)});
  }

  checkout(giaTien:number):Observable<any>{
    const auth = sessionStorage.getItem('token')
    return this.http.post<any>(`${this.API}create-payment-url/servicePackageCode`, {
      "vnp_Version":"2.1.0",
      "vnp_Command":"pay",
      "vnp_Amount":giaTien,
      "vnp_Locale":"vi",
    }, {headers: new HttpHeaders().set('Authorization', `Bearer ${auth}`)});
  }

  checkPayment(param:String):Observable<any>{
    const auth = sessionStorage.getItem('token')
    return this.http.post<any>(`${this.API}verify-payment-result?${param}`, { },
    {headers: new HttpHeaders().set('Authorization', `Bearer ${auth}`)});
  }

  saveOrder(orders:OrdersModel):Observable<any>{
    const auth = sessionStorage.getItem('token')
    return this.http.post<any>(`${this.API}orders/checkout`, orders, {headers: new HttpHeaders().set('Authorization', `Bearer ${auth}`)});
  }

}
