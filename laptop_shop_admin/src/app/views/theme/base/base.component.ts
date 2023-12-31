import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
// Object-Path
import * as objectPath from 'object-path';
// Layout
import {LayoutConfigService, MenuConfigService, PageConfigService} from '../../../core/_base/layout';
import {HtmlClassService} from '../html-class.service';
import {LayoutConfig} from '../../../core/_config/layout.config';
import {MenuConfig} from '../../../core/_config/menu.config';
import {PageConfig} from '../../../core/_config/page.config';
// User permissions
import {NgxPermissionsService} from 'ngx-permissions';
import {AuthService, Permission} from '../../../core/auth';
import {HttpClient} from '@angular/common/http';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-base',
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BaseComponent implements OnInit, OnDestroy {
  // Public variables
  selfLayout = 'default';
  asideSelfDisplay: true;
  contentClasses = '';
  contentContainerClasses = '';
  subheaderDisplay = true;
  contentExtended: false;

  private unsubscribe: Subscription[] = []; // Read more: => https://brianflove.com/2016/12/11/anguar-2-unsubscribe-observables/
  private currentUserPermissions$: Observable<Permission[]>;

  constructor(
    private layoutConfigService: LayoutConfigService,
    private menuConfigService: MenuConfigService,
    private pageConfigService: PageConfigService,
    private htmlClassService: HtmlClassService,
    private permissionsService: NgxPermissionsService,
    private authService: AuthService,
    private http: HttpClient
  ) {
    // register configs by demos
    this.layoutConfigService.loadConfigs(new LayoutConfig().configs);
    this.menuConfigService.loadConfigs(new MenuConfig().configs);
    this.pageConfigService.loadConfigs(new PageConfig().configs);

    // setup element classes
    this.htmlClassService.setConfig(this.layoutConfigService.getConfig());

    const subscription = this.layoutConfigService.onConfigUpdated$.subscribe(layoutConfig => {
      // reset body class based on global and page level layout config, refer to html-class.service.ts
      document.body.className = '';
      this.htmlClassService.setConfig(layoutConfig);
    });
    this.unsubscribe.push(subscription);
  }

  ngOnInit(): void {
    const perm = this.authService.currentUserValue.authorities;

    this.permissionsService.loadPermissions(perm);

    this.http.get('url').subscribe((permissions: any) => {
      this.permissionsService.loadPermissions(permissions);
    })

    const config = this.layoutConfigService.getConfig();
    // Load UI from Layout settings
    this.selfLayout = objectPath.get(config, 'self.layout');
    this.asideSelfDisplay = objectPath.get(config, 'aside.self.display');
    // this.subheaderDisplay = objectPath.get(config, 'subheader.display');
    this.contentClasses = this.htmlClassService.getClasses('content', true).toString();
    this.contentContainerClasses = this.htmlClassService.getClasses('content_container', true).toString();
    this.contentExtended = objectPath.get(config, 'content.extended');

    // let the layout type change
    const subscription = this.layoutConfigService.onConfigUpdated$.subscribe(cfg => {
      setTimeout(() => {
        this.selfLayout = objectPath.get(cfg, 'self.layout');
      });
    });
    this.unsubscribe.push(subscription);
  }

  ngOnDestroy(): void {
    this.unsubscribe.forEach(sb => sb.unsubscribe());
    // https://www.npmjs.com/package/ngx-permissions
    this.permissionsService.flushPermissions();
  }

  /**
   * NGX Permissions, init roles
   */
  loadRolesWithPermissions() {
  }
}
