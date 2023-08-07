import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
// Loading bar
import {LoadingBarService} from '@ngx-loading-bar/core';
import {Observable} from 'rxjs';
// Portlet
import {PortletBodyComponent} from './portlet-body.component';
import {PortletHeaderComponent} from './portlet-header.component';
import {PortletFooterComponent} from './portlet-footer.component';
// Layout
import {LayoutConfigService} from '../../../../../core/_base/layout';

export interface PortletOptions {
  test?: any;
}

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-portlet',
  templateUrl: './portlet.component.html',
  exportAs: 'ktPortlet'
})
export class PortletComponent implements OnInit, AfterViewInit {
  @Input() loading$: Observable<boolean>;
  // portlet extra options
  @Input() options: PortletOptions;
  // portlet root classes
  @Input() class: string;
  @ViewChild('portlet', {static: true}) portlet: ElementRef;
  // portlet header component template
  @ViewChild(PortletHeaderComponent, {static: true}) header: PortletHeaderComponent;
  // portlet body component template
  @ViewChild(PortletBodyComponent, {static: true}) body: PortletBodyComponent;
  // portlet footer component template
  @ViewChild(PortletFooterComponent, {static: true}) footer: PortletFooterComponent;

  constructor(private el: ElementRef, public loader: LoadingBarService,
              private layoutConfigService: LayoutConfigService) {
    this.loader.complete();
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
  }
}
