import {
  Component,
  Inject,
  OnInit,
  ChangeDetectionStrategy,
} from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';
import { delay } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-action-natification',
  templateUrl: './action-notification.component.html',
  changeDetection: ChangeDetectionStrategy.Default,
})
export class ActionNotificationComponent implements OnInit {
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any) {}
  ngOnInit() {
    if (
      !this.data.showUndoButton ||
      this.data.undoButtonDuration >= this.data.duration
    ) {
      return;
    }
    this.delayForUndoButton(this.data.undoButtonDuration).subscribe(() => {
      this.data.showUndoButton = false;
    });
  }

  delayForUndoButton(timeToDelay) {
    return of('').pipe(delay(timeToDelay));
  }

  onDismissWithAction() {
    this.data.snackBar.dismiss();
  }

  public onDismiss() {
    this.data.snackBar.dismiss();
  }
}
