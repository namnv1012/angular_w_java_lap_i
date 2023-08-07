import {Injectable} from '@angular/core';
import {IConfiguration} from '../_models/configuration.model';

import {Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {StorageService} from './storage.service';

@Injectable({providedIn: 'root'})
export class ConfigurationService {
  serverSettings: IConfiguration;
  private settingsLoadedSource = new Subject();
  isReady = false;

  constructor(
    private http: HttpClient,
    private storageService: StorageService
  ) {
  }

  load() {
    this.serverSettings = {identityUrl: ''}
    this.storageService.store('identityUrl', this.serverSettings.identityUrl);
    this.isReady = true;
    this.settingsLoadedSource.next();
  }
}
