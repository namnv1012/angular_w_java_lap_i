import {Component, HostBinding, Input, OnInit} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-portlet-footer',
  template: `
    <ng-content></ng-content>`
})
export class PortletFooterComponent implements OnInit {
  @HostBinding('class') classList = 'card-footer';
  @Input() class: string;

  ngOnInit() {
    if (this.class) {
      this.classList += ' ' + this.class;
    }
  }
}
