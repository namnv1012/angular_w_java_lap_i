import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ProductModel} from "../../model/product.model";
import {CartModel} from "../../model/cart.model";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {CustomerService} from "../../service/customer.service";

@Component({
  selector: 'app-productsingle',
  templateUrl: './productsingle.component.html',
  styleUrls: ['./productsingle.component.scss']
})
export class ProductsingleComponent implements OnInit {
  queryParam: any;
  idProduct: number = 0;
  product: ProductModel = new ProductModel();
  lstProduct: any;
  lstCart: Array<CartModel> = new Array<CartModel>();
  lstCartSession: any = [];
  tab = 1;
  activeImage = 1;

  constructor(private toastr: ToastrService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private changeDetectorRef: ChangeDetectorRef,
              private customerService: CustomerService) {
    this.activatedRoute.queryParams.subscribe(param => {
      this.queryParam = param;
    });
    this.idProduct = this.queryParam.id;
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params: any) => {
      console.log(params);
      this.idProduct = params.get("id");
      this.findProductId();
    });
  }

  addCart() {
    this.lstCartSession = JSON.parse(<string>sessionStorage.getItem('cart'));
    const cart: CartModel = new CartModel();
    cart.productCode = this.product.code;
    cart.quantity = this.product.quantity;
    cart.productName = this.product.name;
    cart.productPrice = this.product.price;
    cart.productId = this.product.id;
    cart.productImage = this.product.imageUrl;
    cart.sale = this.product.sale;
    console.log(this.lstCartSession);
    if (this.lstCartSession !== null) {
      const find = this.lstCartSession.findIndex((x: any) => x.productId === cart.productId);
      if (find >= 0) {
        this.lstCartSession[find].quantity += this.product.quantity;
      }
      else {
        this.lstCartSession.push(cart);
      }
      this.lstCart = this.lstCartSession;
    } else {
      this.lstCart.push(cart);
    }
    // @ts-ignore
    console.log(this.lstCart);
    this.toastr.success('Thêm sản phẩm vào giỏ hàng thành công!');
    sessionStorage.setItem('cart', JSON.stringify(this.lstCart));
    window.location.reload();
  }

  pay() {
    this.lstCartSession = JSON.parse(<string>sessionStorage.getItem('cart'));
    const cart: CartModel = new CartModel();
    cart.productCode = this.product.code;
    cart.quantity = this.product.quantity;
    cart.productName = this.product.name;
    cart.productPrice = this.product.price;
    cart.productId = this.product.id;
    cart.productImage = this.product.imageUrl;
    cart.sale = this.product.sale;
    console.log(this.lstCartSession);
    if (this.lstCartSession !== null) {
      this.lstCartSession.forEach((e:any) => {
        if (e.productId === cart.productId) {
          // @ts-ignore
          cart.quantity = e.quantity + this.product.quantity;
        } else {
          this.lstCartSession.push(cart);
        }
      })
      this.lstCart = this.lstCartSession;
    } else {
      this.lstCart.push(cart);
    }
    // @ts-ignore
    sessionStorage.setItem('cart', JSON.stringify(this.lstCart));
    this.router.navigate(['/checkout'])
  }

  findProductId() {
    this.customerService.getProductDetail(this.idProduct).subscribe(res => {
      this.product = res;
      this.product.quantity = 1;
      this.product.priceSale = Number(this.product.price) - (Number(this.product.price) * Number(this.product.sale) / 100);
      this.customerService.getListProductByProducerId(this.product.producerCode).subscribe(res2 => {
        this.lstProduct = res2;
        this.changeDetectorRef.detectChanges();
      })
      this.changeDetectorRef.detectChanges();
    })
  }

  convertPrice(priceSell: any) {
    return Number(priceSell).toLocaleString('en-US')
  }

  routerProductDetail(id: number) {
    this.router.navigate([{id}]);
    this.changeDetectorRef.detectChanges();
  }

  changeTab(tabIndex: number) {
    this.tab = tabIndex;
    console.log(this.tab);
    this.changeDetectorRef.detectChanges();
  }

  changeImage(index: number) {
    this.activeImage = index;
    console.log(this.activeImage);
    this.changeDetectorRef.detectChanges();
  }
}
