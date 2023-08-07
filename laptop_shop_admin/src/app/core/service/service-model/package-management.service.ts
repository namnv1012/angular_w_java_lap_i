import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environments/environment';


@Injectable({
  providedIn: 'root'
})

export class PackageManagementService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  export(dataSearch: any) {
    return this.httpClient.post(
      `${this.API}register-packages/export`,
      {...dataSearch},
      {responseType: 'blob'}
    );
  }
}
