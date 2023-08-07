import {AfterViewInit, Directive, ElementRef, Input} from '@angular/core';
import * as objectPath from 'object-path';

export interface MenuOptions {
  scroll?: any;
  submenu?: any;
  accordion?: any;
  dropdown?: any;
}

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[ktMenu]',
  exportAs: 'ktMenu',
})
export class MenuDirective implements AfterViewInit {
  @Input() options: MenuOptions;
  private menu: any;

  constructor(private el: ElementRef) {
  }

  ngAfterViewInit(): void {
    this.setupOptions();
    this.menu = new KTMenu(this.el.nativeElement, this.options);
  }

  private setupOptions() {
    let menuDesktopMode = 'accordion';
    if (this.el.nativeElement.getAttribute('data-menu-dropdown') === '1') {
      menuDesktopMode = 'dropdown';
    }

    if (typeof objectPath.get(this.options, 'submenu.desktop') === 'object') {
      objectPath.set(this.options, 'submenu.desktop', menuDesktopMode);
    }
  }
}
