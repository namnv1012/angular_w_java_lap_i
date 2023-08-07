import {Component, OnInit} from '@angular/core';
import {LayoutConfigService} from '../../../core/_base/layout';
// Object-Path
import * as objectPath from 'object-path';
import {HtmlClassService} from '../html-class.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-subheader',
  templateUrl: './subheader.component.html',
})
export class SubheaderComponent implements OnInit {
  layout = 'subheader-v1';
  width = 'fluid';
  clear = false;
  displayDesc = false;
  displayDaterangepicker = true;
  fixed = true;
  style = 'solid';
  subheaderClasses = '';
  subheaderContainerClasses = '';

  constructor(private layoutConfigService: LayoutConfigService, private uiService: HtmlClassService) {
  }

  ngOnInit(): void {
    const config = this.layoutConfigService.getConfig();
    this.layout = objectPath.get(config, 'subheader.layout');
    this.fixed = objectPath.get(config, 'subheader.fixed');
    this.clear = objectPath.get(config, 'subheader.clear');
    this.style = objectPath.get(config, 'subheader.style');
    this.displayDesc = objectPath.get(config, 'subheader.displayDesc');
    this.displayDaterangepicker = objectPath.get(config, 'subheader.displayDaterangepicker');
    this.subheaderClasses = this.uiService.getClasses('subheader', true).toString();
    this.subheaderContainerClasses = this.uiService.getClasses('subheader_container', true).toString()
  }
}
