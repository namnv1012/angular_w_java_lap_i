import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-create-update-user',
  templateUrl: './create-update-user.component.html',
  styleUrls: ['./create-update-user.component.scss']
})
export class CreateUpdateUserComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder,
              public dialogRef: MatDialogRef<CreateUpdateUserComponent>) {
  }

  ngOnInit(): void {
  }

  submit() {
  }

  cancel() {
    this.dialogRef.close();
  }

}
