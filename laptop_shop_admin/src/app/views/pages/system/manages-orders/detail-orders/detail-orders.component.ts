import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NO_ROW_GRID_TEMPLATE} from '../../../../../helpers/constants';
import {MatDialog} from '@angular/material/dialog';
import {CategoryService} from '../../../../../core/service/service-model/category.service';
import {OrdersService} from '../../../../../core/service/service-model/orders.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-detail-orders',
  templateUrl: './detail-orders.component.html',
  styleUrls: ['./detail-orders.component.scss']
})
export class DetailOrdersComponent implements OnInit {

  columnDefs;
  rowData;
  gridApi;
  gridColumnApi;
  headerHeight = 56;
  rowHeight = 50;
  _pageSize = 10;
  _page = 1;
  totalPage = 0;
  totalCategory;
  overlayNoRowsTemplate = 'Không có thông tin';
  lstOrders: any = [];
  orderId;
  queryParam;
  ordes = {
    name: null,
    phone: null,
    address: null,
    status: null
  };
  constructor(private matDialog: MatDialog,
              private changeDetectorRef: ChangeDetectorRef,
              private categoryService: CategoryService,
              private orderService: OrdersService,
              private router: Router,
              private toaStr: ToastrService,
              private activatedRoute: ActivatedRoute) {
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
        headerName: 'Mã sản phẩm',
        field: 'productCode',
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
        tooltipField: 'productCode',
      },
      {
        headerName: 'Tên sản phẩm',
        field: 'productName',
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
        tooltipField: 'productName',
      },
      {
        headerName: 'Số lượng',
        field: 'quantity',
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
        tooltipField: 'quantity',
      },
      {
        headerName: 'Giá',
        field: 'productPrice',
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
        tooltipField: 'productPrice',
      },
    ];
    this.overlayNoRowsTemplate = NO_ROW_GRID_TEMPLATE.replace(
      '{{field}}',
      'Không có thông tin'
    );
    this.activatedRoute.queryParams.subscribe(param => {
      this.queryParam = param;
    });
    if(undefined !== this.queryParam.orderId){
      this.orderId = this.queryParam.orderId;
    }
  }

  ngOnInit(): void {
    this.findById();
    this.loadData();
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

  loadData(){
    this.orderService.getListOrderItemByOrderId(this.orderId).subscribe(res =>{
      this.lstOrders = res;
      this.changeDetectorRef.detectChanges();
    })
  }

  findById(){
    this.orderService.findById(this.orderId).subscribe(res =>{
      this.ordes = res;
      this.changeDetectorRef.detectChanges();
    })
  }

  acessOrder(){
    const orders: any = {};
    orders.id = this.orderId;
    orders.name = this.ordes.name;
    orders.phone = this.ordes.phone;
    this.orderService.changeStatusOrder(orders).subscribe(res =>{
      if(res !== null){
        this.toaStr.success('Duyệt đơn hàng thành công!!!');
        this.router.navigate(['/system/manages-orders'])
      }else{
        this.toaStr.error('Duyệt đơn hàng thất bại !!!');
      }
    })
  }
}
