import {Component, OnInit} from '@angular/core';
import {ICellRendererAngularComp} from 'ag-grid-angular';
import {PopupConfirmComponent} from '../../popup-confirm/popup-confirm.component';
import {MatDialog} from '@angular/material/dialog';
import {CreateUpdateProducerComponent} from '../create-update-producer/create-update-producer.component';
import {ManagesProducerComponent} from '../manages-producer.component';
import {ToastrService} from 'ngx-toastr';
import {ProducerService} from '../../../../../core/service/service-model/producer.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-action-manages-producer',
  templateUrl: './action-manages-producer.component.html',
  styleUrls: ['./action-manages-producer.component.scss']
})
export class ActionManagesProducerComponent implements OnInit, ICellRendererAngularComp {

  cellValue;
  rowIndex;
  producer: any;

  constructor(private matDialog: MatDialog,
              private managesProducer: ManagesProducerComponent,
              private producerService: ProducerService,
              private toast: ToastrService) {

  }

  ngOnInit(): void {
  }

  agInit(params): void {
    this.cellValue = params;
    this.rowIndex = +params.rowIndex + 1;
    this.producer = params.data;
  }

  refresh(params) {
    return true;
  }

  openUpdateProducer() {
    const dataClass: any = {};
    dataClass.action = 'update';
    dataClass.data = this.producer;
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
      this.managesProducer.findProducer(1);
    });
  }

  openDeleteProducer() {
    const dataConfirm = {title: 'Xóa nhà sản xuất', message: 'Bạn có muốn xóa nhà sản xuất?'};
    this.matDialog.open(PopupConfirmComponent, {
      data: dataConfirm,
      disableClose: true,
      hasBackdrop: true,
      width: '420px'
    }).afterClosed().subscribe(res => {
      if (res.event === 'confirm') {
        this.producerService.deleteProducerById(this.producer.id).subscribe(res => {
          if (res !== null) {
            this.toast.success('Xóa thành công!');
            this.managesProducer.findProducer(1);
          } else {
            this.toast.error('Xóa thất bại!');
          }
        });
      }
    });
  }
}
