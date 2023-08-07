import {AfterViewInit, Component, ElementRef, Input, OnInit} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-code-preview',
  templateUrl: './code-preview.component.html',
  styleUrls: ['./code-preview.component.scss'],
})
export class CodePreviewComponent implements OnInit, AfterViewInit {
  @Input() viewItem: any;

  constructor(private el: ElementRef) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    const elements = this.el.nativeElement.querySelectorAll('.example.example-compact');
    KTLayoutExamples.init(elements);
  }
}
