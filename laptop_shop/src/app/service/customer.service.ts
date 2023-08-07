import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private http: HttpClient) {
  }

  getDataHome(): Observable<any> {
    return this.http.get<any>(`${this.API}customer/home`);
  }

  getProductDetail(id: number): Observable<any> {
    return this.http.get<any>(`${this.API}customer/phone-detail/${id}`);
  }

  getListProduct(): Observable<any> {
    return this.http.get<any>(`${this.API}customer/list-product`);
  }

  getAllProducer(): Observable<any> {
    return this.http.get<any>(`${this.API}customer/list-producer`);
  }

  getListProductByProducerId(id: string | undefined): Observable<any> {
    return this.http.get<any>(`${this.API}customer/find-by-producer/${id}`)
  }

  getListProductByProducerCode(code: string): Observable<any> {
    return this.http.get<any>(`${this.API}customer/find-product-by-producerCode/${code}`)
  }
}
