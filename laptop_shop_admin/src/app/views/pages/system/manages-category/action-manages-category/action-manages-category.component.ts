import {Component, OnInit} from '@angular/core';
import {ICellRendererAngularComp} from 'ag-grid-angular';
import {PopupConfirmComponent} from '../../popup-confirm/popup-confirm.component';
import {MatDialog} from '@angular/material/dialog';
import {CategoryService} from '../../../../../core/service/service-model/category.service';
import {ToastrService} from 'ngx-toastr';
import {CreateUpdateCategoryComponent} from '../create-update-category/create-update-category.component';
import {ManagesCategoryComponent} from '../manages-category.component';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-action-manages-category',
  templateUrl: './action-manages-category.component.html',
  styleUrls: ['./action-manages-category.component.scss']
})
export class ActionManagesCategoryComponent implements OnInit, ICellRendererAngularComp {

  cellValue;
  rowIndex;
  category: any;

  constructor(private matDialog: MatDialog,
              private categoryService: CategoryService,
              private managesCategory: ManagesCategoryComponent,
              private toast: ToastrService) {
  }

  ngOnInit(): void {
  }

  agInit(params): void {
    this.cellValue = params;
    this.rowIndex = +params.rowIndex + 1;
    this.category = params.data;
  }

  refresh(params) {
    return true;
  }

  openUpdateCategory() {
    const dataClass: any = {};
    dataClass.action = 'update';
    dataClass.data = this.category;
    this.matDialog.open(
      CreateUpdateCategoryComponent, {
        data: dataClass,
        maxHeight: window.innerHeight + 'px',
        disableClose: true,
        hasBackdrop: true,
        width: '480px',
        autoFocus: false,
      }
    ).afterClosed().subscribe(res => {
      if (res.event === 'add') {
        this.managesCategory.findCategory(1);
      }
    });
  }

  openDeleteCategory() {
    const dataConfirm = {title: 'Xóa danh mục', message: 'Bạn có muốn xóa danh mục?'};
    this.matDialog.open(PopupConfirmComponent, {
      data: dataConfirm,
      disableClose: true,
      hasBackdrop: true,
      width: '420px'
    }).afterClosed().subscribe(res => {
      if (res.event === 'confirm') {
        // Call API
        // tslint:disable-next-line:no-shadowed-variable
        this.categoryService.deleteCategory(this.category.id).subscribe(res => {
          if (res !== null) {
            this.toast.success('Xóa thành công!');
            this.managesCategory.findCategory(1);
          } else {
            this.toast.error('Xóa thất bại!');
          }
        });
      }
    });
  }

}
