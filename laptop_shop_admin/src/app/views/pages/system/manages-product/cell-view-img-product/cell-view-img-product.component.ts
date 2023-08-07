import {Component, OnInit} from '@angular/core';
import {ICellRendererAngularComp} from 'ag-grid-angular';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-cell-view-img-product',
  templateUrl: './cell-view-img-product.component.html'
})
export class CellViewImgProductComponent implements OnInit, ICellRendererAngularComp {
  cellValue;
  rowIndex;
  product;

  constructor() {
  }

  ngOnInit(): void {
  }

  agInit(params): void {
    this.cellValue = params;
    this.rowIndex = +params.rowIndex + 1;
    this.product = params.data;
  }

  refresh(params) {
    return true;
  }
}
