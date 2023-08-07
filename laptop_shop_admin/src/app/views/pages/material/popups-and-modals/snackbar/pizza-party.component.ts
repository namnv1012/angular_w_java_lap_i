import {Component} from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
	selector: 'kt-snack-bar-component-example-snack',
	template: `
	<span class="example-pizza-party">Pizza party!!! 🍕</span>
	`,
	styles: [`.example-pizza-party { color: hotpink; }`],
  })
export class PizzaPartyComponent { }
