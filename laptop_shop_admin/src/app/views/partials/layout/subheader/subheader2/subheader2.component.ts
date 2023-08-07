import {AfterViewInit, Component, Input, OnDestroy, OnInit} from '@angular/core';
// RxJS
import {Subscription} from 'rxjs';
// Layout
import {SubheaderService} from '../../../../../core/_base/layout';
import {Breadcrumb} from '../../../../../core/_base/layout/services/subheader.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-subheader2',
  templateUrl: './subheader2.component.html',
  styleUrls: ['./subheader2.component.scss']
})
export class Subheader2Component implements OnInit, OnDestroy, AfterViewInit {
  @Input() fixed = true;
  @Input() clear = false;
  @Input() width = 'fluid';
  @Input() subheaderClasses = '';
  @Input() subheaderContainerClasses = '';
  @Input() displayDesc = false;
  @Input() displayDaterangepicker = true;

  today: number = Date.now();
  title = '';
  desc = '';
  breadcrumbs: Breadcrumb[] = [];

  private subscriptions: Subscription[] = [];

  constructor(public subheaderService: SubheaderService) {
  }

  ngOnInit() {
  }

  /**
   * After view init
   */
  ngAfterViewInit(): void {
    this.subscriptions.push(this.subheaderService.title$.subscribe(bt => {
      // breadcrumbs title sometimes can be undefined
      if (bt) {
        Promise.resolve(null).then(() => {
          this.title = bt.title;
          this.desc = bt.desc;
        });
      }
    }));

    this.subscriptions.push(this.subheaderService.breadcrumbs$.subscribe(bc => {
      Promise.resolve(null).then(() => {
        this.breadcrumbs = bc;
      });
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sb => sb.unsubscribe());
  }
}
