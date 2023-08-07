import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
// Layout
import {OffcanvasOptions} from '../../../../core/_base/layout';
import {currentUser, Logout, User} from '../../../../core/auth';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-quick-user-panel',
  templateUrl: './quick-user-panel.component.html',
  styleUrls: ['./quick-user-panel.component.scss']
})
export class QuickUserPanelComponent implements OnInit {
  user$: Observable<User>;
  offcanvasOptions: OffcanvasOptions = {
    overlay: true,
    baseClass: 'offcanvas',
    placement: 'right',
    closeBy: 'kt_quick_user_close',
    toggleBy: 'kt_quick_user_toggle'
  };

  constructor() {
  }

  ngOnInit(): void {
  }

  logout() {
  }
}
