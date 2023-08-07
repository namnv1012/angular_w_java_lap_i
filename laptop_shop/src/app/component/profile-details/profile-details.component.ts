import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-profile-details',
  templateUrl: './profile-details.component.html',
})
export class ProfileDetailsComponent implements OnInit{
  userLogin: any = [];
  constructor() { }

  ngOnInit(): void {
    this.userLogin = JSON.parse(<string>localStorage.getItem('userLogin'));
  }
}
