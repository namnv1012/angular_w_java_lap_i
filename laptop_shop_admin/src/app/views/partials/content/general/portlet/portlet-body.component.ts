import {Component, HostBinding, Input, OnInit} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-portlet-body',
  template: `
    <ng-content></ng-content>`
})
export class PortletBodyComponent implements OnInit {
  @HostBinding('class') classList = 'card-body';
  @Input() class: string;

  ngOnInit() {
    if (this.class) {
      this.classList += ' ' + this.class;
    }
  }
}
