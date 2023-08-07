import {ChangeDetectorRef, Component, Inject, OnInit, Optional} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {ProductService} from '../../../../../core/service/service-model/product.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-import-quantity-product',
  templateUrl: './import-quantity-product.component.html',
  styleUrls: ['./import-quantity-product.component.scss']
})
export class ImportQuantityProductComponent implements OnInit {

  form: FormGroup;
  lstProduct;

  constructor(public dialogRef: MatDialogRef<ImportQuantityProductComponent>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: any,
              private fb: FormBuilder,
              private toaStr: ToastrService,
              private productService: ProductService,
              private changeDetectorRef: ChangeDetectorRef) {
    this.buildForm();
  }

  ngOnInit(): void {
    this.getListProduct();
  }

  buildForm() {
    this.form = this.fb.group({
      code: new FormControl('', [Validators.required]),
      quantity: new FormControl('', [Validators.required]),
    })
  }

  submit() {
    const product: any = {};
    product.code = this.getControl.code.value;
    product.quantity = this.getControl.quantity.value;
    this.productService.importQuantity(product).subscribe(res => {
      if (res !== null) {
        this.toaStr.success('Nhập sản phẩm thành công!!!');
        this.dialogRef.close({event: 'add'});
        return;
      } else {
        this.toaStr.error('Nhập sản phẩm thất bại!!!')
      }
    })
  }

  cancel() {
    this.dialogRef.close();
  }

  get getControl() {
    return this.form.controls;
  }

  getListProduct() {
    this.productService.getAll().subscribe(res => {
      this.lstProduct = res;
      this.changeDetectorRef.detectChanges();
    })
  }
}
