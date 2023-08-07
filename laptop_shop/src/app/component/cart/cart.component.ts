import {Component, OnInit} from '@angular/core';
import {CartModel} from "../../model/cart.model";
import {Router} from "@angular/router";
import {ToastrService} from 'ngx-toastr';
import {CheckoutService} from '../../service/checkout.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
})
export class CartComponent implements OnInit {
  lstCart: any = [];
  totalPriceCart = 0;

  constructor(private router: Router,
              private toastr: ToastrService,
              private _checkoutService: CheckoutService) {
  }

  ngOnInit(): void {
    this.lstCart = JSON.parse(<string>sessionStorage.getItem('cart'));
    this.lstCart.forEach((e : any)=> {
      this.totalPriceCart = this.totalPriceCart + (Number(e.productPrice - e.productPrice*e.sale/100) * Number(e.quantity));
    })
    const url = window.location.href;
    var param = url.substring(27, 1000);
    console.log(param)
    if (param) {
      this._checkoutService.checkPayment(param).subscribe((result) => {
        if (result && result.status == 200) {
          this.toastr.success(`${result.message}`)
          this.lstCart.splice(0, 999);
        }
      });
    }

  }
  routerProductDetail(id: number | undefined) {
    this.router.navigate(['/product'], {
      queryParams: {
        id: id
      }
    })
  }

  totalProduct(quantity: number | undefined, price: number | undefined) {
    if (quantity === undefined || price === undefined)
      return 0;
    else
      return quantity * price;
  }

  convertPrice(priceSell: any) {
    return Number(priceSell).toLocaleString('en-US')
  }

  changeQuantity(productId: number | undefined) {

  }

  deleteProduct(i: number) {
    this.lstCart.splice(i, 1);
    sessionStorage.setItem('cart', JSON.stringify(this.lstCart));
    window.location.reload();
  }

  sendToPay(giaTien: number) {
    this._checkoutService.checkout(giaTien).subscribe((result) => {
      if (result && result.status == 200) {
        var x = result.data;
        window.location.replace(result.data);
      }
    });
  }

}
