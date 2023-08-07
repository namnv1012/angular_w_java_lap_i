// Angular
import {Component, Input} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-widget3-authors',
  templateUrl: './widget3-authors.component.html'
})
export class Widget3NewArrivalsAuthorsComponent {
  @Input() cssClasses = '';
}
