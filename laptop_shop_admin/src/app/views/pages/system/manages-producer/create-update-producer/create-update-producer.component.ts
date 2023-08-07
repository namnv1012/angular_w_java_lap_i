import {ChangeDetectorRef, Component, Inject, OnInit, Optional} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {ProducerService} from '../../../../../core/service/service-model/producer.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-create-update-producer',
  templateUrl: './create-update-producer.component.html',
  styleUrls: ['./create-update-producer.component.scss']
})
export class CreateUpdateProducerComponent implements OnInit {

  form: FormGroup;
  action;
  producer;
  loading = false;

  constructor(public dialogRef: MatDialogRef<CreateUpdateProducerComponent>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: any,
              private fb: FormBuilder,
              private toaStr: ToastrService,
              private producerService: ProducerService,
              private changeDetectorRef: ChangeDetectorRef) {
    this.action = data.action;
    this.producer = data.data;
    console.log(this.producer);
    this.buildForm();
  }

  ngOnInit(): void {
    this.loadForm();
  }

  buildForm() {
    this.form = this.fb.group({
      code: new FormControl('', [Validators.required, Validators.maxLength(50)], this.producerService.validateCode(this.producer?.id)),
      name: new FormControl('', [Validators.required, Validators.maxLength(250)]),
    })
  }

  submit() {
    this.loading = true;
    const producer: any = {};
    if (this.action === 'update') {
      producer.id = this.producer.id;
    }
    producer.code = this.getControl.code.value;
    producer.name = this.getControl.name.value;
    this.producerService.addProducer(producer).subscribe(res => {
      this.loading = false;
      if (res !== null) {
        if (this.action === 'create') {
          this.toaStr.success('Thêm mới nhà sản xuất thành công')
        } else {
          this.toaStr.success('Cập nhật nhà sản xuất thành công');
        }
        this.dialogRef.close({event: 'add'});
        return;
      } else {
        if (this.action === 'create') {
          this.toaStr.error('Thêm mới nhà sản xuất thất bại')
        } else {
          this.toaStr.error('Cập nhật nhà sản xuất thất bại');
        }
      }
    }, error => {
      this.loading = false;
    });
  }

  cancel() {
    this.dialogRef.close();
  }

  get getControl() {
    return this.form.controls;
  }

  loadForm() {
    if (this.action === 'update') {
      // tslint:disable-next-line:forin
      for (const controlName in this.form.controls) {
        this.form.get(controlName).setValue(this.producer[controlName]);
      }
      this.form.controls.code.setErrors({codeExitsts: false});
      console.log(this.form);
    }
  }
}
