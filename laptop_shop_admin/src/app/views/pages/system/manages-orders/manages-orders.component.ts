import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {listOrder, NO_ROW_GRID_TEMPLATE} from '../../../../helpers/constants';
import * as moment from 'moment';
import {SearchCategoryModel} from '../../../../core/service/model/search-category.model';
import {MatDialog} from '@angular/material/dialog';
import {CategoryService} from '../../../../core/service/service-model/category.service';
import {CommonServiceService} from '../../../../core/service/utils/common-service.service';
import {CreateUpdateCategoryComponent} from '../manages-category/create-update-category/create-update-category.component';
import {ActionDetailOrderComponent} from './action-detail-order/action-detail-order.component';
import {OrdersService} from '../../../../core/service/service-model/orders.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-manages-orders',
  templateUrl: './manages-orders.component.html',
  styleUrls: ['./manages-orders.component.scss']
})
export class ManagesOrdersComponent implements OnInit {

  columnDefs;
  rowData;
  gridApi;
  gridColumnApi;
  headerHeight = 56;
  rowHeight = 50;
  _pageSize = 10;
  _page = 1;
  totalSchool = 0;
  first = 1;
  last = 10;
  total = 0;
  totalPage = 0;
  totalCategory;
  overlayNoRowsTemplate = 'Không có thông tin';
  showPadding = true;
  rangeWithDots = [];
  lstOrders: any = [];
  listStatus = listOrder;
  searchCategory: SearchCategoryModel = new SearchCategoryModel();
  totalOrders;
  constructor(private matDialog: MatDialog,
              private changeDetectorRef: ChangeDetectorRef,
              private categoryService: CategoryService,
              private orderService: OrdersService,
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
        headerName: 'Mã khách hàng',
        field: 'userCode',
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
        headerName: 'Tên khách hàng',
        field: 'name',
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
        headerName: 'Số điên thoại',
        field: 'phone',
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
        headerName: 'Ngày đặt',
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
        // tooltipField: 'createDate',
      },
      {
        headerName: 'Trạng thái',
        valueGetter: param => {
          return param.data == null ? '' : param.data.status === 1 ? 'Đã duyệt' : 'Chờ duyệt'
        },
        tooltipValueGetter: param => {
          return param.data == null ? '' : param.data.status === 1 ? 'Đã duyệt' : 'Chờ duyệt'
        },
        cellStyle: param=>{
          let color = '';
          if(param.data.status === false){
            color = '#D14343';
          }else{
            color = '#52BD94';
          }
          return{
            'font-weight': '500',
            'font-size': '12px',
            top: '13px',
            'white-space': 'nowrap',
            'text-overflow': 'ellipsis',
            overflow: 'hidden',
            color,
          }
        },
        minWidth: 126
      },
      {
        headerName: '',
        suppressMovable: true,
        field: '',
        cellRendererFramework: ActionDetailOrderComponent,
        minWidth: 200,
        cellStyle:{
          display: 'flex',
          'align-items': 'center',
        }
      },
    ];
    this.overlayNoRowsTemplate = NO_ROW_GRID_TEMPLATE.replace(
      '{{field}}',
      'Không có thông tin'
    );
    this.searchCategory.status = 0;
  }

  ngOnInit(): void {
    this.findOrders(1);
    // this.loadData();
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

  openCreate(){
    const dataClass : any = {};
    dataClass.action = 'create';
    this.matDialog.open(
      CreateUpdateCategoryComponent,{
        data: dataClass,
        maxHeight: window.innerHeight + 'px',
        disableClose: true,
        hasBackdrop: true,
        width: '480px',
        autoFocus: false,
      }
    ).afterClosed().subscribe(res => {
      if(res.event === 'add'){
        this.findOrders(1);
      }
    });
  }

  findOrders(page: number) {
    this._page = page;
    this.orderService.searchOrders(this.searchCategory, page, this._pageSize).subscribe(res => {
      this.lstOrders = res.lstOrders;
      if (this.lstOrders.length === 0) {
        this.showPadding = false;
      } else {
        this.showPadding = true;
      }
      this.totalOrders = res.total;
      this.first = ((page - 1) * this._pageSize) + 1;
      this.last = this.first + this.lstOrders.length - 1;
      if (this.totalOrders % this._pageSize === 0) {
        this.totalPage = Math.floor(this.totalOrders / this._pageSize);
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      } else {
        this.totalPage = Math.floor(this.totalOrders / this._pageSize) + 1;
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      }
      this.gridApi.setRowData(this.lstOrders);
      this.changeDetectorRef.detectChanges();
    });
  }

  page(page: number): void {
    this._page = page
    this.findOrders(page);
  }

  prev(): void {
    this._page--
    if (this._page < 1) {
      this._page = 1
      return
    }
    this.findOrders(this._page);
  }

  next(): void {
    this._page++
    if (this._page > this.totalPage) {
      this._page = this.totalPage;
      return;
    }
    this.findOrders(this._page);
  }

  loadData(){
    this.orderService.getAll().subscribe(res =>{
      this.lstOrders = res;
      this.changeDetectorRef.detectChanges();
    })
  }
}
