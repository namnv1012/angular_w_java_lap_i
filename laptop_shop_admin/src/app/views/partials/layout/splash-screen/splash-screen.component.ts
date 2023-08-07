import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
// Object-Path
import * as objectPath from 'object-path';
// Layout
import {
  LayoutConfigService,
  SplashScreenService,
} from '../../../../core/_base/layout';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-splash-screen',
  templateUrl: './splash-screen.component.html',
  styleUrls: ['./splash-screen.component.scss'],
})
export class SplashScreenComponent implements OnInit {
  loaderType: string;
  @ViewChild('splashScreen', {static: true}) splashScreen: ElementRef;

  constructor(
    private el: ElementRef,
    private layoutConfigService: LayoutConfigService,
    private splashScreenService: SplashScreenService
  ) {
  }

  ngOnInit() {
    const loaderConfig = this.layoutConfigService.getConfig('loader');
    this.loaderType = objectPath.get(loaderConfig, 'page-loader.type');
    this.splashScreenService.init(this.splashScreen);
  }
}
