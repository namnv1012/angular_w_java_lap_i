import { AfterViewInit, Component, Input, OnInit } from "@angular/core";

@Component({
  selector: "kt-cart",
  templateUrl: "./cart.component.html",
})
export class CartComponent implements OnInit, AfterViewInit {
  @Input() icon = "flaticon2-shopping-cart-1";
  @Input() iconType: "" | "brand";
  @Input() useSVG: boolean;
  @Input() bgImage: string;

  constructor() {}

  ngAfterViewInit(): void {}

  ngOnInit(): void {}
}
