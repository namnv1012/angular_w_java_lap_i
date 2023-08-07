import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {
  lstProductCart: any = [];
  userLogin: any = [];
  totalPriceCart = 0;
  isLogin: boolean = false;
  keySearch: any;
  constructor(private router: Router, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    this.lstProductCart = JSON.parse(<string>sessionStorage.getItem('cart'));
    if(this.lstProductCart !== null){
      this.lstProductCart.forEach((e: any)=> {
        this.totalPriceCart = this.totalPriceCart + (Number(e.quantity) * Number(e.productPrice - e.productPrice*e.sale/100));
      })
    }
    this.userLogin = JSON.parse(<any>localStorage.getItem('userLogin'));
    console.log(this.userLogin);
    if (this.userLogin){
      this.isLogin = true;
    }
    console.log(this.lstProductCart);
  }

  /* Chuyển hướng đến trang login nếu tài khoản này chưa từng login*/
  loginClick() {
    const userLogin = localStorage.getItem('userLogin');
    if (userLogin === null || userLogin === undefined) {
      this.router.navigate(['/login']);
    }
  }

  routerProductDetail(id: number | undefined) {
    this.router.navigate(['/product'], {
      queryParams: {
        id: id
      }
    })
  }

  convertPrice(priceSell: any) {
    return Number(priceSell).toLocaleString('en-US')
  }

  routerProfile(){
    console.log(this.userLogin)
    if(this.userLogin !== null && this.userLogin !== undefined){
      this.router.navigate(['/profile-details'])
    }
  }

  logout(){
    localStorage.removeItem('userLogin');
    localStorage.removeItem('currentUser');
    sessionStorage.removeItem('token');
    window.location.reload();
    this.toastr.success('Đăng xuất thành công!')
  }

  search(){
    this.router.navigate(['/shop'], {
      queryParams: {
        productName: this.keySearch
      }
    });
  }
}
