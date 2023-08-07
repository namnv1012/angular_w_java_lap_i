import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NO_ROW_GRID_TEMPLATE} from '../../../../../helpers/constants';
import {MatDialog} from '@angular/material/dialog';
import * as moment from 'moment';
import {WarrantyDetailsService} from '../../../../../core/service/service-model/warranty-details.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-list-warranty-details',
  templateUrl: './list-warranty-details.component.html',
  styleUrls: ['./list-warranty-details.component.scss']
})
export class ListWarrantyDetailsComponent implements OnInit {

  columnDefs;
  rowData;
  gridApi;
  gridColumnApi;
  headerHeight = 56;
  rowHeight = 50;
  _pageSize = 10;
  _page = 1;
  overlayNoRowsTemplate = 'Không có thông tin';
  lstWarrantyDetails: any = [];
  queryParam;
  warrantyId;

  constructor(private matDialog: MatDialog,
              private changeDetectorRef: ChangeDetectorRef,
              private warrantyDetailService: WarrantyDetailsService,
              private router: Router,
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
        headerName: 'Ngày nhận sản phẩm',
        field: 'receivedDate',
        cellRenderer: (param) => {
          return `${moment(param.data.receivedDate).format('DD/MM/YYYY')}`;
        },
        tooltipValueGetter: (param) => {
          return `${moment(param.data.receivedDate).format('DD/MM/YYYY')}`;
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
        headerName: 'Ngày trả sản phẩm',
        field: 'payDate',
        cellRenderer: (param) => {
          return `${moment(param.data.payDate).format('DD/MM/YYYY')}`;
        },
        tooltipValueGetter: (param) => {
          return `${moment(param.data.payDate).format('DD/MM/YYYY')}`;
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
        headerName: 'Ghi chú tình trạng sản phẩm',
        field: 'note',
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
        tooltipField: 'note',
      },
    ];
    this.overlayNoRowsTemplate = NO_ROW_GRID_TEMPLATE.replace(
      '{{field}}',
      'Không có thông tin'
    );
    this.activatedRoute.queryParams.subscribe(param => {
      this.queryParam = param;
    });
    if (undefined !== this.queryParam.id) {
      this.warrantyId = this.queryParam.id;
    }
  }

  ngOnInit(): void {
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

  loadData() {
    this.warrantyDetailService.findByWarrantyId(this.warrantyId).subscribe(res => {
      this.lstWarrantyDetails = res;
      this.gridApi.setRowData(this.lstWarrantyDetails);
      this.changeDetectorRef.detectChanges();
    });
  }

}
