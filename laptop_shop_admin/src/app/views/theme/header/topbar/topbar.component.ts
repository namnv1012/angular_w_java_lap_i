import {Component} from '@angular/core';
import {LayoutConfigService} from '../../../../core/_base/layout';
import {SecurityService} from '../../../../core/auth/_services/security.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss'],
})
export class TopbarComponent {
  searchDisplay = true;
  notificationsDisplay = true;
  quickActionsDisplay = true;
  cartDisplay = true;
  quickPanelDisplay = true;
  languagesDisplay = true;
  userLayout = 'offcanvas';
  userDropdownStyle = 'light';

  constructor(private layoutConfigService: LayoutConfigService, private securityService: SecurityService,) {
    this.searchDisplay = this.layoutConfigService.getConfig('extras.search.display');
    this.notificationsDisplay = this.layoutConfigService.getConfig('extras.notifications.display');
    this.quickActionsDisplay = this.layoutConfigService.getConfig('extras.quick-actions.display');
    this.cartDisplay = this.layoutConfigService.getConfig('extras.cart.display');
    this.quickPanelDisplay = this.layoutConfigService.getConfig('extras.quick-panel.display');
    this.languagesDisplay = this.layoutConfigService.getConfig('extras.languages.display');
    this.userLayout = this.layoutConfigService.getConfig('extras.user.layout');
    this.userDropdownStyle = this.layoutConfigService.getConfig('extras.user.dropdown.style');
  }

  logout() {
    this.securityService.logoff();
  }
}
