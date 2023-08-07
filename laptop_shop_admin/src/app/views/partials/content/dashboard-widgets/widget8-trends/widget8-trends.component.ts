// Angular
import { Component, Input, OnInit } from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-widget8-trends',
  templateUrl: './widget8-trends.component.html'
})
export class Widget8TrendsComponent {
  @Input() cssClasses = '';

  constructor() {
  }
}
