import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

/* Dependency injection:
  - Nói về việc class A phụ thuộc vào class B
  - Thêm các metadata vào class AuthService
*/
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  /*
    - module Http sử dụng observables để xử lý các request
    - module form, router thì sử dụng observable để lắng sự kiện người dùng như nhập thông tin form.
  */
  createAccount(user: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}account/register-customer`, user);
  }
}
