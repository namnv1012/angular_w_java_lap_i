import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-action-detail-order',
  templateUrl: './action-detail-order.component.html',
})
export class ActionDetailOrderComponent implements OnInit {

  rowIndex;
  cellValue;
  order;
  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  viewOrderDetail(){
    this.router.navigate(['/system/order-detail'], {
      queryParams: {
        orderId: this.order.id,
      }
    });
  }

  agInit(params ): void {
    this.rowIndex = +params.rowIndex + 1;
    this.cellValue = params;
    this.order = params.data;
  }

  refresh(params) {
    return true;
  }

}
