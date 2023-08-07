import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { select, Store } from '@ngrx/store';
// State
import { currentUser, Logout, User } from '../../../../../core/auth';

@Component({
  selector: 'kt-user-profile',
  templateUrl: './user-profile.component.html',
})
export class UserProfileComponent implements OnInit {
  user$: Observable<User>;
  @Input() userDropdownStyle = 'light';
  @Input() avatar = true;
  @Input() greeting = true;
  @Input() badge: boolean;
  @Input() icon: boolean;

  constructor() {
  }

  ngOnInit(): void {
  }

  logout() {
  }
}
