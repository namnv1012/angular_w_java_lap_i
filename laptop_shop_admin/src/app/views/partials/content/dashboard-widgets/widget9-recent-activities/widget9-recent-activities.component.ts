// Angular
import {Component, Input, OnInit} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-widget9-recent-activities',
  templateUrl: './widget9-recent-activities.component.html'
})
export class Widget9RecentActivitiesComponent {
  @Input() cssClasses = '';

  constructor() {
  }
}
