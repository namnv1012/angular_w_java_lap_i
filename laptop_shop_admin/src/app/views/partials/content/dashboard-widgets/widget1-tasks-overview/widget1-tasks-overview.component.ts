import {Component, Input, OnInit} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-widget1-tasks-overview',
  templateUrl: './widget1-tasks-overview.component.html'
})
export class Widget1TasksOverviewComponent {
  @Input() cssClasses = '';

  constructor() {
  }
}
