import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';

@Injectable()
export class MenuConfigService {
  onConfigUpdated$: Subject<any>;
  private menuConfig: any;

  constructor() {
    // register on config changed event and set default config
    this.onConfigUpdated$ = new Subject();
  }

  /**
   * Returns the menuConfig
   */
  getMenus() {
    return this.menuConfig;
  }

  /**
   * Load config
   *
   * @param config: any
   */
  loadConfigs(config: any) {
    this.menuConfig = config;
    this.onConfigUpdated$.next(this.menuConfig);
  }
}
