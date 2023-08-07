import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {list_status, NO_ROW_GRID_TEMPLATE} from '../../../../helpers/constants';
import * as moment from 'moment';
import {MatDialog} from '@angular/material/dialog';
import {CommonServiceService} from '../../../../core/service/utils/common-service.service';
import {CreateUpdateCategoryComponent} from './create-update-category/create-update-category.component';
import {ActionManagesCategoryComponent} from './action-manages-category/action-manages-category.component';
import {CategoryService} from '../../../../core/service/service-model/category.service';
import {SearchCategoryModel} from '../../../../core/service/model/search-category.model';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-manages-category',
  templateUrl: './manages-category.component.html',
  styleUrls: ['./manages-category.component.scss']
})
export class ManagesCategoryComponent implements OnInit {

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
  lstCategory: any = [];
  listStatus = list_status;
  searchCategory: SearchCategoryModel = new SearchCategoryModel();

  constructor(private matDialog: MatDialog,
              private changeDetectorRef: ChangeDetectorRef,
              private categoryService: CategoryService,
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
        headerName: 'Mã danh mục',
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
        headerName: 'Tên danh mục',
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
      },
      {
        headerName: '',
        suppressMovable: true,
        field: '',
        cellRendererFramework: ActionManagesCategoryComponent,
        minWidth: 50,
        maxWidth: 50,
      },
    ];
    this.overlayNoRowsTemplate = NO_ROW_GRID_TEMPLATE.replace(
      '{{field}}',
      'Không có thông tin'
    );
    this.searchCategory.status = 1;
  }

  ngOnInit(): void {
    this.findCategory(1);
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
    const dataClass: any = {};
    dataClass.action = 'create';
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
        this.findCategory(1);
      }
    });
  }

  findCategory(page: number) {
    this._page = page;
    this.categoryService.searchCategory(this.searchCategory, page, this._pageSize).subscribe(res => {
      this.lstCategory = res.lstCategory;
      if (this.lstCategory.length === 0) {
        this.showPadding = false;
      } else {
        this.showPadding = true;
      }
      this.totalCategory = res.total;
      this.first = ((page - 1) * this._pageSize) + 1;
      this.last = this.first + this.lstCategory.length - 1;
      if (this.totalCategory % this._pageSize === 0) {
        this.totalPage = Math.floor(this.totalCategory / this._pageSize);
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      } else {
        this.totalPage = Math.floor(this.totalCategory / this._pageSize) + 1;
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      }
      this.gridApi.setRowData(this.lstCategory);
      this.changeDetectorRef.detectChanges();
    });
  }

  page(page: number): void {
    this._page = page
    this.findCategory(page);
  }

  prev(): void {
    this._page--
    if (this._page < 1) {
      this._page = 1
      return
    }
    this.findCategory(this._page);
  }

  next(): void {
    this._page++
    if (this._page > this.totalPage) {
      this._page = this.totalPage;
      return;
    }
    this.findCategory(this._page);
  }
}
