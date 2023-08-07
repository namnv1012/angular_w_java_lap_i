import {AfterViewInit, Directive, ElementRef, Input} from '@angular/core';

export interface OffcanvasOptions {
  baseClass: string;
  placement?: string;
  overlay?: boolean;
  closeBy: string;
  toggleBy?: any;
}

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[ktOffcanvas]',
  exportAs: 'ktOffcanvas',
})
export class OffcanvasDirective implements AfterViewInit {
  @Input() options: OffcanvasOptions;
  private offcanvas: any;

  constructor(private el: ElementRef) {
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.offcanvas = new KTOffcanvas(this.el.nativeElement, this.options);
    });
  }
}
