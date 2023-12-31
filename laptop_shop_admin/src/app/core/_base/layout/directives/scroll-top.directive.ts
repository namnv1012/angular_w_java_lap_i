import {AfterViewInit, Directive, ElementRef, Input} from '@angular/core';

export interface ScrollTopOptions {
  offset: number;
  speed: number;
}

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[ktScrollTop]'
})
export class ScrollTopDirective implements AfterViewInit {
  @Input() options: ScrollTopOptions;
  private scrollTop: any;

  constructor(private el: ElementRef) {
  }

  ngAfterViewInit(): void {
    this.scrollTop = new KTScrolltop(this.el.nativeElement, this.options);
  }
}
