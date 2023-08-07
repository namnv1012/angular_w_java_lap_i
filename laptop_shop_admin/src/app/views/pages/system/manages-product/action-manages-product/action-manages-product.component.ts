import {Component, OnInit} from '@angular/core';
import {ICellRendererAngularComp} from 'ag-grid-angular';
import {Router} from '@angular/router';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-action-manages-product',
  templateUrl: './action-manages-product.component.html',
  styleUrls: ['./action-manages-product.component.scss']
})
export class ActionManagesProductComponent implements OnInit, ICellRendererAngularComp {

  cellValue;
  rowIndex;
  product: any;

  constructor(private router: Router) {
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

  openUpdateProduct() {
    this.router.navigate(['/system/create-update-product'], {
      queryParams: {
        id: this.product.id,
      }
    });
  }

  openDeleteProduct() {
  }
}
