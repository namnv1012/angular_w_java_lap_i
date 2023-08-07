import {
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import {DashboardService} from '../../../../core/service/service-model/dashboard.service';
import * as $ from 'jquery';
import {Chart} from 'node_modules/chart.js/dist/Chart.min.js';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  school;
  hide = true;
  image = {
    image1: '',
    image2: '',
    image3: '',
  }
  minWidth1 = 15;
  minWidth2 = 15;
  schoolGrowthRate;
  userGrowthRate;
  revenueGrowthRate;

  totalOrder;
  totalCustomer;
  totalEmployee;
  totalPrice;

  constructor(private dashboardService: DashboardService,
              private changeDetectorRef: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.dashboardService.getAll().subscribe(res => {
      this.totalOrder = res.totalOrder;
      this.totalCustomer = res.totalCustomer;
      this.totalEmployee = res.totalEmployee;
      this.totalPrice = res.totalPrice;
      this.changeDetectorRef.detectChanges();
    }),
      this.dashboardService.getAllOderItem().subscribe(res => {
        let chartContainer = $('#BestSeller');
        if (chartContainer.length === 0) {
          return;
        }
        let chart = new Chart(chartContainer, {
          type: 'bar',
          data: {
            datasets: [{
              label: 'Số lượng',
              data: [res[0].amount, res[1].amount, res[2].amount, res[3].amount, res[4].amount],
              backgroundColor: ['#34bfa3', '#716aca', '#00c5dc', '#FF9966', '#0000FF'],
              borderColor: ['#34bfa3', '#716aca', '#00c5dc', '#FF9966', '#0000FF'],
            }
            ],
            labels: [res[0].productCode, res[1].productCode, res[2].productCode, res[3].productCode, res[4].productCode],
          },
          options: {
            title: {
              text: 'Biểu Đồ Thống Kê Sản Phẩm Bán Chạy',
              display: true,
            },
            tooltips: {
              intersect: false,
              mode: 'point',
              xPadding: 10,
              yPadding: 10,
              caretPadding: 10
            },
            legend: {
              display: false
            },
            responsive: true,
            maintainAspectRatio: false,
            barRadius: 0,
            scales: {
              xAxes: [{
                display: true,
                gridLines: true,
                stacked: false
              }],
              yAxes: [{
                display: true,
                stacked: false,
                gridLines: true
              }]
            },
            layout: {
              padding: {
                left: 0,
                right: 100,
                top: 0,
                bottom: 20
              }
            }
          }
        });
      }),
      this.dashboardService.getAllProductInventory().subscribe(res => {
        let chartContainer = $('#Inventory');
        if (chartContainer.length === 0) {
          return;
        }
        let chart = new Chart(chartContainer, {
          type: 'bar',
          data: {
            labels: [res[0].code, res[1].code, res[2].code, res[3].code, res[4].code],
            datasets: [{
              label: 'Số lượng',
              data: [res[0].amount, res[1].amount, res[2].amount, res[3].amount, res[4].amount],
              backgroundColor: ['#FF0000', '#FF0000', '#FF0000', '#FF0000', '#FF0000'],
              borderColor: ['#FF0000', '#FF0000', '#FF0000', '#FF0000', '#FF0000'],
            },
            ]
          },
          options: {
            title: {
              text: 'Biểu Đồ Thống Kê Sản Phẩm Tồn Kho',
              display: true,
            },
            tooltips: {
              intersect: false,
              mode: 'point',
              xPadding: 10,
              yPadding: 10,
              caretPadding: 10
            },
            legend: {
              display: false
            },
            responsive: true,
            maintainAspectRatio: false,
            barRadius: 0,
            scales: {
              xAxes: [{
                display: true,
                gridLines: true,
                stacked: false
              }],
              yAxes: [{
                display: true,
                stacked: false,
                gridLines: true
              }]
            },
            layout: {
              padding: {
                left: 100,
                right: 0,
                top: 0,
                bottom: 20
              }
            }
          }
        });
      })
  }
}
