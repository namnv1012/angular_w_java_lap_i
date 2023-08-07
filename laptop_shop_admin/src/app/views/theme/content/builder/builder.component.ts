import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {LayoutConfigModel, LayoutConfigService} from '../../../../core/_base/layout';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-builder',
  templateUrl: './builder.component.html',
  styleUrls: ['./builder.component.scss'],
})
export class BuilderComponent implements OnInit {
  model: LayoutConfigModel;
  @ViewChild('form', {static: true}) form: NgForm;

  constructor(private layoutConfigService: LayoutConfigService, private el: ElementRef) {
  }

  ngOnInit(): void {
    this.model = this.layoutConfigService.getConfig();
    const elements = this.el.nativeElement.querySelectorAll('.example');
    KTLayoutExamples.init(elements);
  }

  resetPreview(e: Event): void {
    e.preventDefault();
    this.layoutConfigService.resetConfig();
    location.reload();
  }

  submitPreview(e: Event): void {
    this.layoutConfigService.setConfig(this.model, true);
    location.reload();
  }
}
