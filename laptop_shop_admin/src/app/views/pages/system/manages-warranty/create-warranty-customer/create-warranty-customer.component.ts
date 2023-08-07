import {ChangeDetectorRef, Component, Inject, OnInit, Optional} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {WarrantyDetailsService} from '../../../../../core/service/service-model/warranty-details.service';
import {WarrantyService} from '../../../../../core/service/service-model/warranty.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-create-warranty-customer',
  templateUrl: './create-warranty-customer.component.html',
  styleUrls: ['./create-warranty-customer.component.scss']
})
export class CreateWarrantyCustomerComponent implements OnInit {

  form: FormGroup;
  action;
  warranty;
  lstProduct;

  constructor(public dialogRef: MatDialogRef<CreateWarrantyCustomerComponent>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: any,
              private fb: FormBuilder,
              private toaStr: ToastrService,
              private changeDetectorRef: ChangeDetectorRef,
              private warrantyService: WarrantyService,
              private warrantyDetailsService: WarrantyDetailsService) {
    this.warranty = data.data;
    this.buildForm();
  }

  ngOnInit(): void {
    this.getListProductByWarrantyId();
  }

  buildForm() {
    this.form = this.fb.group({
      productCode: new FormControl('', [Validators.required]),
      receivedDate: new FormControl(new Date(), [Validators.required]),
      payDate: new FormControl('', [Validators.required]),
      note: new FormControl('', [Validators.required]),
    })
  }

  submit() {
    const warrantyDetail: any = {};
    warrantyDetail.warrantyId = this.warranty.id;
    warrantyDetail.productCode = this.getControl.productCode.value;
    warrantyDetail.note = this.getControl.note.value;
    warrantyDetail.dateReceived = this.getControl.receivedDate.value;
    warrantyDetail.datePay = this.getControl.payDate.value;
    this.warrantyDetailsService.addWarrantyDetails(warrantyDetail).subscribe(res => {
      if (res !== null) {
        this.toaStr.success('Thêm mới bảo hành khách hàng thành công')
        this.dialogRef.close({event: 'add'});
        return;
      } else {
        this.toaStr.error('Thêm mới bảo hành khách hàng thất bại')
      }
    })
  }

  cancel() {
    this.dialogRef.close();
  }

  get getControl() {
    return this.form.controls;
  }

  getListProductByWarrantyId() {
    this.warrantyService.getListProductById(this.warranty.id).subscribe(res => {
      this.lstProduct = res;
      this.changeDetectorRef.detectChanges();
    })
  }

}
