import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-view-warranty-customer',
  templateUrl: './view-warranty-customer.component.html'
})
export class ViewWarrantyCustomerComponent implements OnInit {

  rowIndex;
  cellValue;
  warranty;

  constructor(private router: Router) {
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

  viewWarrantyCustomer() {
    this.router.navigate(['/system/list-warranty-details'], {
      queryParams: {
        id: this.warranty.id
      }
    });
  }
}
