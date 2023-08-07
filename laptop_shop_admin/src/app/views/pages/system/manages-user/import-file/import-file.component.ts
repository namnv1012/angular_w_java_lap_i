import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-import-file',
  templateUrl: './import-file.component.html',
  styleUrls: ['./import-file.component.scss']
})
export class ImportFileComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<ImportFileComponent>) {
  }

  ngOnInit(): void {
  }

  downloadSampleFile() {
  }

  onFileInput($event) {
  }

  importFile() {
  }

  cancel() {
    this.dialogRef.close();
  }
}
