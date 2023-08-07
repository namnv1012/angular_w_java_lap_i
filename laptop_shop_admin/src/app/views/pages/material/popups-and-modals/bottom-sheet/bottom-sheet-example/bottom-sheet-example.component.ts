import { Component } from '@angular/core';
import {MatBottomSheetRef } from '@angular/material/bottom-sheet';


@Component({
  // tslint:disable-next-line:component-selector
	selector: 'kt-bottom-sheet-example',
	templateUrl: './bottom-sheet-example.component.html'
})
export class BottomSheetExampleComponent {
	constructor(private bottomSheetRef: MatBottomSheetRef<BottomSheetExampleComponent>) {}

	openLink(event: MouseEvent): void {
	  this.bottomSheetRef.dismiss();
	  event.preventDefault();
	}
}
