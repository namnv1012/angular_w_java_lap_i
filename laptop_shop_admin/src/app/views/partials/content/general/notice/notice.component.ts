import {
  Component,
  OnInit,
  Input,
  ChangeDetectionStrategy,
} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-notice',
  templateUrl: './notice.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NoticeComponent implements OnInit {
  @Input() classes: string;
  @Input() icon: string;
  @Input() svg: string;

  constructor() {
  }

  ngOnInit() {
  }
}
