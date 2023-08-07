import {Directive, Input, OnInit} from '@angular/core';

export interface ScrollTopOptions {
  offset: number;
  speed: number;
}

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[indexFocus]'
})
export class FieldFocusDirective implements OnInit {
  @Input() indexFocus;

  constructor() {
  }

  ngOnInit(): void {
    const interval = setInterval(() => {
      if (this.indexFocus) {
        this.indexFocus.focus();
        clearInterval(interval);
      }
    }, 200);
  }
}
