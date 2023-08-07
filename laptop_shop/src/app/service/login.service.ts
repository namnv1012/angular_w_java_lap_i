import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {map} from "rxjs";

const CURRENT_NAME = "currentUser";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private API = `${environment.API_GATEWAY_ENDPOINT}`;
  private returnUrl: string | undefined;
  public currentUser: any;

  constructor(private http: HttpClient, private router: Router) {
  }

  login(username: string, password: string) {
    return this.http.post<any>(`${this.API}authenticate`, {username: username, password: password})
      .pipe(map(user => {
        console.log(user.token);
        if (user && user.token) {
          localStorage.setItem(CURRENT_NAME, JSON.stringify(user.currentUser));
          sessionStorage.setItem(environment.authTokenKey, user.token);
          this.currentUser = JSON.stringify(user.currentUser);
          console.log(sessionStorage.getItem(environment.authTokenKey));
        }
        return user;
      }));
  }

  public logout() {
    sessionStorage.removeItem(environment.authTokenKey);
    localStorage.removeItem(CURRENT_NAME);
    this.router.navigate(['/'], {queryParams: {returnUrl: this.returnUrl}});
  }
}
