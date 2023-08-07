import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {CartModel} from "../../model/cart.model";
import {OrdersModel} from "../../model/orders.model";
import {OrderItemModel} from "../../model/orderItem.model";
import {ToastrService} from "ngx-toastr";
import {CheckoutService} from "../../service/checkout.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
})
export class CheckoutComponent implements OnInit{
  userLogin: any;
  formOrder: FormGroup = new FormGroup({});
  cart: CartModel = new CartModel();
  orders: OrdersModel = new OrdersModel();
  orderItem: OrderItemModel = new OrderItemModel();
  lstOrderItem: Array<OrderItemModel> = new Array<OrderItemModel>();
  lstCart: any = [];
  totalPriceCart = 0;

  constructor(private toastr: ToastrService,
              private fb: FormBuilder,
              private checkoutService: CheckoutService,
              private router: Router) {
    this.buildForm();
  }

  ngOnInit(): void {
    this.isLogin();
    this.getCart();
    const url = window.location.href;
    var param = url.substring(49, 1000);
    console.log(param)
    if (param) {
      this.checkoutService.checkPayment(param).subscribe((result) => {
        console.log(result);
        if (result && result.status == 200) {
          sessionStorage.removeItem('cart');
          this.toastr.success('Đặt hàng thành công!')
          setTimeout(function(){
            window.location.reload();
          },3000);
          this.router.navigate(['/']);
        } else {
          this.toastr.error('Đặt hàng thất bại!')
        }
      });
    }
  }

  buildForm() {
    this.formOrder = this.fb.group({
      name: new FormControl(''),
      phone: new FormControl(''),
      email: new FormControl(''),
      address: new FormControl(''),
      note: new FormControl('')
    })
  }

  isLogin() {
    this.userLogin = localStorage.getItem('userLogin')
    if (this.userLogin === null || this.userLogin === undefined) {
      this.router.navigate(['/login']);
    }
  }

  checkout(totalPriceCart :number) {
    const auth = sessionStorage.getItem('token');
    if(auth == null) {
      this.toastr.error('Vui lòng đăng nhập');
      localStorage.setItem('userLogin', '');
      this.router.navigate(['/login']);
    }else{
      this.orders.name = this.f['name'].value;
      this.orders.phone = this.f['phone'].value;
      this.orders.email = this.f['email'].value;
      this.orders.address = this.f['address'].value;
      this.orders.note = this.f['note'].value;
      this.orders.lstOrderItem = this.lstCart;
      this.checkoutService.checkout(totalPriceCart).subscribe(res =>{
        if (res && res.status == 200) {
          var x = res.data;
          this.checkoutService.saveOrder(this.orders).subscribe(resAPI =>{
            console.log(resAPI);
          })
          window.location.replace(res.data);
        }
      })
    }
  }

  getCart() {
    // @ts-ignore
    this.cart = JSON.parse(sessionStorage.getItem('cart'));
    this.lstCart = JSON.parse(<string>sessionStorage.getItem('cart'));
    this.lstCart.forEach((e: any) => {
      this.totalPriceCart = this.totalPriceCart + (Number(e.productPrice - e.productPrice*e.sale/100) * Number(e.quantity));
    })
    this.orderItem.productCode = this.cart.productCode;
    this.orderItem.quantity = this.cart.quantity;
    this.lstOrderItem.push(this.orderItem);
  }

  get f() {
    return this.formOrder.controls;
  }

  cvtPrice(priceSell: any) {
    return Number(priceSell).toLocaleString('en-US')
  }
}
