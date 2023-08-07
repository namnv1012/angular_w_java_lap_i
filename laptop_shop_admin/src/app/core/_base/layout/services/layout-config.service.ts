import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import * as objectPath from 'object-path';
import {merge} from 'lodash';
import {LayoutConfigModel} from '../models/layout-config.model';

const localStorageKey = 'layoutConfigV702';

@Injectable()
export class LayoutConfigService {
  onConfigUpdated$: Subject<LayoutConfigModel>;
  layoutConfig: LayoutConfigModel;

  constructor() {
    // register on config changed event and set default config
    this.onConfigUpdated$ = new Subject();
  }

  saveConfig(layoutConfig: LayoutConfigModel): void {
    if (layoutConfig) {
      sessionStorage.setItem(localStorageKey, JSON.stringify(layoutConfig));
    }
  }

  getSavedConfig(): LayoutConfigModel {
    const config = sessionStorage.getItem(localStorageKey);
    try {
      return JSON.parse(config);
    } catch (e) {
    }
  }

  resetConfig(): void {
    sessionStorage.removeItem('layoutConfig');
  }

  getConfig(path?: string): LayoutConfigModel | any {
    // merge default layout config with the saved config from layout storage
    // @todo; known issue; viewing 2 or more demos at the time in different browser's tabs, can cause conflict to the layout config
    this.layoutConfig = this.getSavedConfig();

    if (path) {
      return objectPath.get(this.layoutConfig, path);
    }

    return this.layoutConfig;
  }

  setConfig(value: any, save?: boolean): void {
    this.layoutConfig = merge(this.layoutConfig, value);

    if (save) {
      this.saveConfig(this.layoutConfig);
    }
    this.onConfigUpdated$.next(this.layoutConfig);
  }

  getLogo(): string {
    const menuAsideLeftSkin = objectPath.get(this.layoutConfig, 'brand.self.theme');
    // set brand logo
    const logoObject = objectPath.get(this.layoutConfig, 'self.logo');

    let logo;
    if (typeof logoObject === 'string') {
      logo = logoObject;
    }
    if (typeof logoObject === 'object') {
      logo = objectPath.get(logoObject, menuAsideLeftSkin + '');
    }
    if (typeof logo === 'undefined') {
      try {
        const logos = objectPath.get(this.layoutConfig, 'self.logo');
        logo = logos[Object.keys(logos)[0]];
      } catch (e) {
      }
    }
    return logo;
  }

  loadConfigs(config: LayoutConfigModel) {
    this.layoutConfig = this.getSavedConfig();
    // use saved config as priority, or load new config if demo does not matched
    if (!this.layoutConfig || objectPath.get(this.layoutConfig, 'demo') !== config.demo) {
      this.layoutConfig = config;
    }
    this.saveConfig(this.layoutConfig);
  }
}
