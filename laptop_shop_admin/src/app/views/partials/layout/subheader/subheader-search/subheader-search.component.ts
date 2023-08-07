import {AfterViewInit, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {SubheaderService} from '../../../../../core/_base/layout';
import {Breadcrumb} from '../../../../../core/_base/layout/services/subheader.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-subheader-search',
  templateUrl: './subheader-search.component.html',
  styleUrls: ['./subheader-search.component.scss']
})
export class SubheaderSearchComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() fluid: boolean;
  @Input() clear: boolean;

  today: number = Date.now();
  title = '';
  desc = '';
  breadcrumbs: Breadcrumb[] = [];

  private readonly subscriptions: Subscription[] = [];

  constructor(public subheaderService: SubheaderService) {
  }

  ngOnInit() {
  }

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
