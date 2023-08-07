// Angular
import {Component, Input} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-widget2-new-arrivals',
  templateUrl: './widget2-new-arrivals.component.html',
})
export class Widget2NewArrivalsComponent {
  @Input() cssClasses = '';
  currentTab = 'Day';

  constructor() {
  }

  setCurrentTab(tab: string) {
    this.currentTab = tab;
  }
}
