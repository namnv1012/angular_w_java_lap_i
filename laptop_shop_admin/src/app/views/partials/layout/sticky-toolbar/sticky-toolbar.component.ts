import {Component} from '@angular/core';
// Layout
import {LayoutConfigService, OffcanvasOptions} from '../../../../core/_base/layout';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-sticky-toolbar',
  templateUrl: './sticky-toolbar.component.html',
  styleUrls: ['./sticky-toolbar.component.scss'],
})
export class StickyToolbarComponent {
  demoPanelOptions: OffcanvasOptions = {
    overlay: true,
    baseClass: 'offcanvas',
    placement: 'right',
    closeBy: 'kt_demo_panel_close',
    toggleBy: 'kt_demo_panel_toggle'
  };

  baseHref: string;

  constructor(private layoutConfigService: LayoutConfigService) {
    this.baseHref = 'https://keenthemes.com/metronic/preview/angular/';
  }

  isActiveDemo(demo) {
    return demo === this.layoutConfigService.getConfig('demo');
  }
}
