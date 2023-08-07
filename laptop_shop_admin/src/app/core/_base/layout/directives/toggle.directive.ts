import {AfterViewInit, Directive, ElementRef, Input} from '@angular/core';

export interface ToggleOptions {
  target?: string | any;
  targetState?: string;
  toggleState?: string;
}

/**
 * Toggle
 */
@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[ktToggle]',
  exportAs: 'ktToggle'
})
export class ToggleDirective implements AfterViewInit {
  @Input() options: ToggleOptions;
  toggle: any;

  constructor(private el: ElementRef) {
  }

  ngAfterViewInit(): void {
    this.toggle = new KTToggle(this.el.nativeElement, this.options);
  }
}
