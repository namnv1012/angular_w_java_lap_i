import {Component, OnInit} from '@angular/core';
import {ICellRendererAngularComp} from 'ag-grid-angular';
import {MatDialog} from '@angular/material/dialog';
import {CreateWarrantyCustomerComponent} from '../create-warranty-customer/create-warranty-customer.component';
import {ManagesWarrantyComponent} from '../manages-warranty.component';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-action-manages-warranty',
  templateUrl: './action-manages-warranty.component.html'
})
export class ActionManagesWarrantyComponent implements OnInit, ICellRendererAngularComp {
  rowIndex;
  cellValue;
  warranty;

  constructor(private matDialog: MatDialog, private managesWarranty: ManagesWarrantyComponent) {
  }

  ngOnInit(): void {
  }

  agInit(params): void {
    this.rowIndex = +params.rowIndex + 1;
    this.cellValue = params;
    this.warranty = params.data;
  }

  refresh(params) {
    return true;
  }

  addWarrantyDeail() {
    const dataClass: any = {};
    dataClass.action = 'create';
    dataClass.data = this.warranty;
    this.matDialog.open(
      CreateWarrantyCustomerComponent, {
        data: dataClass,
        maxHeight: window.innerHeight + 'px',
        disableClose: true,
        hasBackdrop: true,
        width: '480px',
        autoFocus: false,
      }
    ).afterClosed().subscribe(res => {
      this.managesWarranty.findWarranty(1);
    });
  }
}
