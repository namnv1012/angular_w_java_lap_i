import {ChangeDetectorRef, Component, Inject, OnInit, Optional} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {CategoryService} from '../../../../../core/service/service-model/category.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-create-update-category',
  templateUrl: './create-update-category.component.html',
  styleUrls: ['./create-update-category.component.scss']
})
export class CreateUpdateCategoryComponent implements OnInit {

  form: FormGroup;
  action;
  category;
  loading = false;
  constructor(public dialogRef: MatDialogRef<CreateUpdateCategoryComponent>,
              @Optional() @Inject(MAT_DIALOG_DATA) public data: any,
              private fb: FormBuilder,
              private toaStr: ToastrService,
              private categoryService: CategoryService,
              private changeDetectorRef: ChangeDetectorRef) {
    this.action = data.action;
    this.category = data.data;
    this.buildForm();
  }

  ngOnInit(): void {
    this.loadForm();
  }

  buildForm() {
    this.form = this.fb.group({
      code: new FormControl('', [Validators.required, Validators.maxLength(50)], this.categoryService.validateCode(this.category?.id)),
      name: new FormControl('', [Validators.required, Validators.maxLength(250)]),
    })
  }

  submit() {
    this.loading = true;
    const category: any = {};
    if (this.action === 'update') {
      category.id = this.category.id;
    }
    category.code = this.getControl.code.value;
    category.name = this.getControl.name.value;
    this.categoryService.addCategory(category).subscribe(res => {
      this.loading = false;
      if (res !== null) {
        if (this.action === 'create') {
          this.toaStr.success('Thêm mới danh mục thành công')
        } else {
          this.toaStr.success('Cập nhật danh mục thành công');
        }
        this.dialogRef.close({event: 'add'});
        return;
      } else {
        if (this.action === 'create') {
          this.toaStr.error('Thêm mới danh mục thất bại')
        } else {
          this.toaStr.error('Cập nhật danh mục thất bại');
        }
      }
    }, error => {
      this.loading = false;
    });
  }

  loadForm() {
    if (this.action === 'update') {
      // tslint:disable-next-line:forin
      for (const controlName in this.form.controls) {
        this.form.get(controlName).setValue(this.category[controlName]);
      }
      this.form.controls.code.setErrors({codeExitsts: false});
      console.log(this.form);
    }
  }

  cancel() {
    this.dialogRef.close();
  }

  get getControl() {
    return this.form.controls;
  }
}
