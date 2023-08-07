import {Injectable} from '@angular/core';
import {StorageService} from './storage.service';
import {Router} from '@angular/router';
import {OAuthService} from 'angular-oauth2-oidc';
import {ConfigurationService} from './configuration.service';
import {UtilityService} from './utility.service';

@Injectable({providedIn: 'root'})
export class SecurityService {
  constructor(
    private oauthService: OAuthService,
    private configurationService: ConfigurationService,
    private utilService: UtilityService,
    private router: Router,
    private storageService: StorageService
  ) {
  }

  public logoff() {
    this.router.navigate(['/auth/login']);
    document.location.reload();
    this.storageService.remove('sessionToken');
  }

}
