import {Component} from '@angular/core';
import {OffcanvasOptions} from '../../../../core/_base/layout';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-quick-panel',
  templateUrl: './quick-panel.component.html',
  styleUrls: ['./quick-panel.component.scss']
})
export class QuickPanelComponent {
  offcanvasOptions: OffcanvasOptions = {
    overlay: true,
    baseClass: 'offcanvas',
    placement: 'right',
    closeBy: 'kt_quick_panel_close',
    toggleBy: 'kt_quick_panel_toggle'
  };
}
