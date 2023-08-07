import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {list_status, NO_ROW_GRID_TEMPLATE} from '../../../../helpers/constants';
import {MatDialog} from '@angular/material/dialog';
import {ProducerService} from '../../../../core/service/service-model/producer.service';
import {CommonServiceService} from '../../../../core/service/utils/common-service.service';
import * as moment from 'moment';
import {WarrantyService} from '../../../../core/service/service-model/warranty.service';
import {ViewWarrantyCustomerComponent} from './view-warranty-customer/view-warranty-customer.component';
import {ActionManagesWarrantyComponent} from './action-manages-warranty/action-manages-warranty.component';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-manages-warranty',
  templateUrl: './manages-warranty.component.html',
  styleUrls: ['./manages-warranty.component.scss']
})
export class ManagesWarrantyComponent implements OnInit {

  columnDefs;
  rowData;
  gridApi;
  gridColumnApi;
  headerHeight = 56;
  rowHeight = 50;
  _pageSize = 10;
  _page = 1;
  first = 1;
  last = 10;
  total = 0;
  totalPage = 0;
  overlayNoRowsTemplate = 'Không có thông tin';
  showPadding = true;
  rangeWithDots = [];
  lstWarranty: any = [];
  listStatus = list_status;
  totalWarranty;

  constructor(private matDialog: MatDialog,
              private changeDetectorRef: ChangeDetectorRef,
              private producerService: ProducerService,
              private warrantyService: WarrantyService,
              private commonService: CommonServiceService) {
    this.columnDefs = [
      {
        headerName: 'STT',
        valueGetter: (param) => {
          return param.node.rowIndex + ((this._page - 1) * this._pageSize + 1);
        },
        minWidth: 60,
        maxWidth: 60,
        cellStyle: {
          'font-weight': '500',
          'font-size': '12px',
          'align-items': 'center',
          color: '#101840',
          display: 'flex',
          left: '6px',
        },
      },
      {
        field: 'id',
        maxWidth: 10,
        minWidth: 10,
        hide: true
      },
      {
        headerName: 'Tên khách hàng',
        field: 'customerName',
        cellStyle: {
          'font-weight': '500',
          'font-size': '12px',
          color: '#3366FF',
          top: '13px',
          'white-space': 'nowrap',
          'text-overflow': 'ellipsis',
          overflow: 'hidden',
        },
        minWidth: 126,
        tooltipField: 'code',
      },
      {
        headerName: 'Số điện thoại',
        field: 'phoneNumber',
        cellStyle: {
          'font-weight': '500',
          'font-size': '12px',
          color: '#101840',
          top: '13px',
          'white-space': 'nowrap',
          'text-overflow': 'ellipsis',
          overflow: 'hidden',
        },
        minWidth: 126,
        tooltipField: 'name',
      },
      {
        headerName: 'Ngày tạo',
        field: 'createDate',
        cellRenderer: (param) => {
          return `${moment(param.data.createDate).format('DD/MM/YYYY')}`;
        },
        tooltipValueGetter: (param) => {
          return `${moment(param.data.createDate).format('DD/MM/YYYY')}`;
        },
        cellStyle: {
          'font-weight': '500',
          'font-size': '12px',
          color: '#101840',
          top: '13px',
          'white-space': 'nowrap',
          'text-overflow': 'ellipsis',
          overflow: 'hidden',
        },
        minWidth: 126,
        tooltipField: 'createDate',
      },
      {
        headerName: 'Ngày hết hạn bảo hành',
        field: 'expirationDate',
        cellRenderer: (param) => {
          return `${moment(param.data.expirationDate).format('DD/MM/YYYY')}`;
        },
        tooltipValueGetter: (param) => {
          return `${moment(param.data.expirationDate).format('DD/MM/YYYY')}`;
        },
        cellStyle: {
          'font-weight': '500',
          'font-size': '12px',
          color: '#101840',
          top: '13px',
          'white-space': 'nowrap',
          'text-overflow': 'ellipsis',
          overflow: 'hidden',
        },
        minWidth: 126,
        tooltipField: 'createDate',
      },
      {
        headerName: 'Lịch sử bảo hành của khách hàng',
        suppressMovable: true,
        field: '',
        cellRendererFramework: ViewWarrantyCustomerComponent,
        minWidth: 200,
        cellStyle: {
          display: 'flex',
          'align-items': 'center',
        }
      },
      {
        headerName: 'Thêm bảo hành',
        suppressMovable: true,
        field: '',
        cellRendererFramework: ActionManagesWarrantyComponent,
        minWidth: 200,
        cellStyle: {
          display: 'flex',
          'align-items': 'center',
        }
      },
    ];
    this.overlayNoRowsTemplate = NO_ROW_GRID_TEMPLATE.replace(
      '{{field}}',
      'Không có thông tin'
    );
  }

  ngOnInit(): void {
    this.findWarranty(1);
  }

  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    params.api.sizeColumnsToFit();
    this.gridApi.setRowData(this.rowData);
  }

  gridSizeChanged(params) {
    params.api.sizeColumnsToFit();
  }

  findWarranty(page: number) {
    this._page = page;
    this.warrantyService.getAll().subscribe(res => {
      this.lstWarranty = res;
      // if (this.lstWarranty.length === 0) {
      //   this.showPadding = false;
      // } else {
      //   this.showPadding = true;
      // }
      // this.totalWarranty = res.total;
      // this.first = ((page - 1) * this._pageSize) + 1;
      // this.last = this.first + this.lstWarranty.length - 1;
      // if (this.totalWarranty % this._pageSize === 0) {
      //   this.totalPage = Math.floor(this.totalWarranty / this._pageSize);
      //   this.rangeWithDots = this.commonService.pagination(
      //     this._page,
      //     this.totalPage
      //   );
      // } else {
      //   this.totalPage = Math.floor(this.totalWarranty / this._pageSize) + 1;
      //   this.rangeWithDots = this.commonService.pagination(
      //     this._page,
      //     this.totalPage
      //   );
      // }
      this.gridApi.setRowData(this.lstWarranty);
      this.changeDetectorRef.detectChanges();
    });
  }

  page(page: number): void {
    this._page = page
    this.findWarranty(page);
  }

  prev(): void {
    this._page--
    if (this._page < 1) {
      this._page = 1
      return
    }
    this.findWarranty(this._page);
  }

  next(): void {
    this._page++
    if (this._page > this.totalPage) {
      this._page = this.totalPage;
      return;
    }
    this.findWarranty(this._page);
  }

}
