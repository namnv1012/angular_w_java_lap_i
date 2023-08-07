import {Component, OnInit} from '@angular/core';
import {HtmlClassService} from '../html-class.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-footer',
  templateUrl: './footer.component.html',
})
export class FooterComponent implements OnInit {
  today: number = Date.now();
  footerClasses = '';
  footerContainerClasses = '';

  constructor(private uiClasses: HtmlClassService) {
  }

  ngOnInit(): void {
    this.footerClasses = this.uiClasses.getClasses('footer', true).toString();
    this.footerContainerClasses = this.uiClasses.getClasses('footer_container', true).toString();
  }
}
