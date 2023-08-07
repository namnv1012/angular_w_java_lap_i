import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
// Layout
import {LayoutConfigService} from '../../../../../core/_base/layout';
// Charts
import {Chart} from 'chart.js/dist/Chart.min.js';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-widget14',
  templateUrl: './widget14.component.html'
})
export class Widget14Component implements OnInit {
  @Input() title: string;
  @Input() desc: string;
  @Input() data: { labels: string[]; datasets: any[] };
  @ViewChild('chart', {static: true}) chart: ElementRef;

  constructor(private layoutConfigService: LayoutConfigService) {
  }

  ngOnInit() {
    if (!this.data) {
      this.data = {
        labels: ['Label 1', 'Label 2', 'Label 3', 'Label 4', 'Label 5', 'Label 6', 'Label 7', 'Label 8', 'Label 9', 'Label 10', 'Label 11', 'Label 12', 'Label 13', 'Label 14', 'Label 15', 'Label 16'],
        datasets: [
          {
            backgroundColor: this.layoutConfigService.getConfig('colors.states.success'),
            data: [
              15, 20, 25, 30, 25, 20, 15, 20, 25, 30, 25, 20, 15, 10, 15, 20
            ]
          }, {
            backgroundColor: '#f3f3fb',
            data: [
              15, 20, 25, 30, 25, 20, 15, 20, 25, 30, 25, 20, 15, 10, 15, 20
            ]
          }
        ]
      };
    }

    this.initChartJS();
  }

  initChartJS() {
    const chart = new Chart(this.chart.nativeElement, {
      type: 'bar',
      data: this.data,
      options: {
        title: {
          display: false,
        },
        tooltips: {
          intersect: false,
          mode: 'nearest',
          xPadding: 10,
          yPadding: 10,
          caretPadding: 10
        },
        legend: {
          display: false
        },
        responsive: true,
        maintainAspectRatio: false,
        barRadius: 4,
        scales: {
          xAxes: [{
            display: false,
            gridLines: false,
            stacked: true
          }],
          yAxes: [{
            display: false,
            stacked: true,
            gridLines: false
          }]
        },
        layout: {
          padding: {
            left: 0,
            right: 0,
            top: 0,
            bottom: 0
          }
        }
      }
    });
  }
}
