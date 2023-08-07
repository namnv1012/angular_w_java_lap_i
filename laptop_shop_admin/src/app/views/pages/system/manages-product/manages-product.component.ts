import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {list_status, NO_ROW_GRID_TEMPLATE} from '../../../../helpers/constants';
import {MatDialog} from '@angular/material/dialog';
import {CommonServiceService} from '../../../../core/service/utils/common-service.service';
import * as moment from 'moment';
import {ProductService} from '../../../../core/service/service-model/product.service';
import {CellViewImgProductComponent} from './cell-view-img-product/cell-view-img-product.component';
import {Router} from '@angular/router';
import {SearchProducer} from '../../../../core/service/model/search-producer';
import {ActionManagesProductComponent} from './action-manages-product/action-manages-product.component';
import {ImportQuantityProductComponent} from './import-quantity-product/import-quantity-product.component';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-manages-product',
  templateUrl: './manages-product.component.html',
  styleUrls: ['./manages-product.component.scss']
})
export class ManagesProductComponent implements OnInit {

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
  overlayNoRowsTemplate = 'Không có thông tin';
  showPadding = true;
  rangeWithDots = [];
  lstProduct: any = [];
  listStatus = list_status;
  totalProduct = 0;
  searchProducer: SearchProducer = new SearchProducer();

  constructor(private matDialog: MatDialog,
              private productService: ProductService,
              private changeDetectorRef: ChangeDetectorRef,
              private router: Router,
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
        headerName: 'Ảnh sản phẩm',
        suppressMovable: true,
        field: '',
        cellRendererFramework: CellViewImgProductComponent,
        minWidth: 200,
        maxWidth: 200,
        cellStyle: {
          display: 'flex',
          'align-items': 'center',
        }
      },
      {
        headerName: 'Mã sản phẩm',
        field: 'code',
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
        headerName: 'Tên sản phẩm',
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
        headerName: 'Loại danh mục',
        field: 'categoryName',
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
        tooltipField: 'categoryName',
      },
      {
        headerName: 'nhà sản xuất',
        field: 'producerName',
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
        tooltipField: 'producerName',
      },
      {
        headerName: 'Khuyến mại',
        field: 'sale',
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
        tooltipField: 'sale',
      },
      {
        headerName: 'Giá bán',
        field: 'price',
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
        tooltipField: 'price',
      },
      {
        headerName: 'Trạng thái',
        valueGetter: param => {
          return param.data == null ? '' : param.data.status === true ? 'Đang hoạt động' : 'Khóa'
        },
        tooltipValueGetter: param => {
          return param.data == null ? '' : param.data.status === true ? 'Đang hoạt động' : 'Khóa'
        },
        cellStyle: param => {
          let color = '';
          if (param.data.status === false) {
            color = '#D14343';
          } else {
            color = '#52BD94';
          }
          return {
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
        headerName: '',
        suppressMovable: true,
        field: '',
        cellRendererFramework: ActionManagesProductComponent,
        minWidth: 50,
        maxWidth: 50,
      },
    ];
    this.overlayNoRowsTemplate = NO_ROW_GRID_TEMPLATE.replace(
      '{{field}}',
      'Không có thông tin'
    );
    this.searchProducer.status = 1;
    this.searchProducer.name = '';
  }

  ngOnInit(): void {
    this.findProduct(1);
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

  openCreate() {
    this.router.navigate(['/system/create-update-product'])
  }

  findProduct(page: number) {
    this._page = page;
    this.productService.searchProduct(this.searchProducer, page, this._pageSize).subscribe(res => {
      this.lstProduct = res.lstProduct;
      if (this.lstProduct.length === 0) {
        this.showPadding = false;
      } else {
        this.showPadding = true;
      }
      this.totalProduct = res.total;
      this.first = ((page - 1) * this._pageSize) + 1;
      this.last = this.first + this.lstProduct.length - 1;
      if (this.totalProduct % this._pageSize === 0) {
        this.totalPage = Math.floor(this.totalProduct / this._pageSize);
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      } else {
        this.totalPage = Math.floor(this.totalProduct / this._pageSize) + 1;
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      }
      this.gridApi.setRowData(this.lstProduct);
      this.changeDetectorRef.detectChanges();
    });
  }

  page(page: number): void {
    this._page = page
    this.findProduct(page);
  }

  prev(): void {
    this._page--
    if (this._page < 1) {
      this._page = 1
      return
    }
    this.findProduct(this._page);
  }

  next(): void {
    this._page++
    if (this._page > this.totalPage) {
      this._page = this.totalPage;
      return;
    }
    this.findProduct(this._page);
  }

  openImportQuantity() {
    const dataClass: any = {};
    dataClass.action = 'create';
    this.matDialog.open(
      ImportQuantityProductComponent, {
        data: dataClass,
        maxHeight: window.innerHeight + 'px',
        disableClose: true,
        hasBackdrop: true,
        width: '480px',
        autoFocus: false,
      }
    ).afterClosed().subscribe(res => {
      if (res.event === 'add') {
        this.findProduct(1);
      }
    });
  }
}
