import {Component} from '@angular/core';
import {ScrollTopOptions} from '../../../../core/_base/layout';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-scroll-top',
  templateUrl: './scroll-top.component.html',
})
export class ScrollTopComponent {
  scrollTopOptions: ScrollTopOptions = {
    offset: 300,
    speed: 600
  };
}
