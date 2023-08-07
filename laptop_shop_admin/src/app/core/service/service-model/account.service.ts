import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {BasicService} from '../utils/basic.service';
import {HelperService} from '../utils/helper.service';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})

export class AccountService extends BasicService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private http: HttpClient, public helperService: HelperService) {
    super(http, helperService);
  }

  getAllProvince(): Observable<any> {
    return this.getRequest(`${this.API}province/get-all`)
  }

  getDistrictByProvince(prId: string): Observable<any> {
    return this.getRequest(`${this.API}province/get-district-of-province/${prId}`)
  }
}
