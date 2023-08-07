import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {list_status, NO_ROW_GRID_TEMPLATE} from '../../../../helpers/constants';
import {MatDialog} from '@angular/material/dialog';
import {CommonServiceService} from '../../../../core/service/utils/common-service.service';
import * as moment from 'moment';
import {CreateUpdateProducerComponent} from './create-update-producer/create-update-producer.component';
import {ActionManagesProducerComponent} from './action-manages-producer/action-manages-producer.component';
import {ProducerService} from '../../../../core/service/service-model/producer.service';
import {SearchProducer} from '../../../../core/service/model/search-producer';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-manages-producer',
  templateUrl: './manages-producer.component.html',
  styleUrls: ['./manages-producer.component.scss']
})
export class ManagesProducerComponent implements OnInit {

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
  lstProducer: any = [];
  listStatus = list_status;
  totalProducer;
  searchProducer: SearchProducer = new SearchProducer();

  constructor(private matDialog: MatDialog,
              private changeDetectorRef: ChangeDetectorRef,
              private producerService: ProducerService,
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
        headerName: 'Mã nhà sản xuất',
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
        headerName: 'Tên nhà sản xuất',
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
        tooltipField: 'createDate',
      },
      {
        headerName: '',
        suppressMovable: true,
        field: '',
        cellRendererFramework: ActionManagesProducerComponent,
        minWidth: 50,
        maxWidth: 50,
      },
    ];
    this.overlayNoRowsTemplate = NO_ROW_GRID_TEMPLATE.replace(
      '{{field}}',
      'Không có thông tin'
    );
    this.searchProducer.status = 1;
  }

  ngOnInit(): void {
    this.findProducer(1);
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
      CreateUpdateProducerComponent, {
        data: dataClass,
        maxHeight: window.innerHeight + 'px',
        disableClose: true,
        hasBackdrop: true,
        width: '480px',
        autoFocus: false,
      }
    ).afterClosed().subscribe(res => {
      if (res.event === 'add') {
        this.findProducer(1);
      }
    });
  }

  findProducer(page: number) {
    this._page = page;
    this.producerService.searchProducer(this.searchProducer, page, this._pageSize).subscribe(res => {
      this.lstProducer = res.lstProducer;
      if (this.lstProducer.length === 0) {
        this.showPadding = false;
      } else {
        this.showPadding = true;
      }
      this.totalProducer = res.total;
      this.first = ((page - 1) * this._pageSize) + 1;
      this.last = this.first + this.lstProducer.length - 1;
      if (this.totalProducer % this._pageSize === 0) {
        this.totalPage = Math.floor(this.totalProducer / this._pageSize);
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      } else {
        this.totalPage = Math.floor(this.totalProducer / this._pageSize) + 1;
        this.rangeWithDots = this.commonService.pagination(
          this._page,
          this.totalPage
        );
      }
      this.gridApi.setRowData(this.lstProducer);
      this.changeDetectorRef.detectChanges();
    });
  }

  page(page: number): void {
    this._page = page
    this.findProducer(page);
  }

  prev(): void {
    this._page--
    if (this._page < 1) {
      this._page = 1
      return
    }
    this.findProducer(this._page);
  }

  next(): void {
    this._page++
    if (this._page > this.totalPage) {
      this._page = this.totalPage;
      return;
    }
    this.findProducer(this._page);
  }
}
