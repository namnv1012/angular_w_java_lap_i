import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {BasicService} from '../utils/basic.service';
import {HelperService} from '../utils/helper.service';


@Injectable({
  providedIn: 'root'
})

export class DashboardService extends BasicService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private http: HttpClient, public helperService: HelperService) {
    super(http, helperService);
  }

  getAll(): Observable<any> {
    return this.httpClient.get<any>(`${this.API}dashboard/load-home`);
  }

  // biểu đồ sản phẩm bán chạy
  getAllOderItem(){
    return this.http.get<any>(`${this.API}get-all`);
  }

  getAllProductInventory(){
    return this.http.get<any>(`${this.API}get-products-inventory`)
  }
}
