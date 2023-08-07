import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-system',
  templateUrl: './system.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SystemComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

}
