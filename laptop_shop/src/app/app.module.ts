import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CartComponent} from './component/cart/cart.component';
import {CheckoutComponent} from './component/checkout/checkout.component';
import {FooterComponent} from './component/footer/footer.component';
import {ForgotPasswordComponent} from './component/forgot-password/forgot-password.component';
import {HeaderComponent} from './component/header/header.component';
import {HomeComponent} from './component/home/home.component';
import {LoginComponent} from './component/login/login.component';
import {ProductsingleComponent} from './component/productsingle/productsingle.component';
import {ProfileDetailsComponent} from './component/profile-details/profile-details.component';
import {ShopComponent} from './component/shop/shop.component';
import {SignupComponent} from './component/signup/signup.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {SlickCarouselModule} from "ngx-slick-carousel";
import {ToastrModule} from "ngx-toastr";
import {CarouselModule} from "ngx-owl-carousel-o";
import {HashLocationStrategy, LocationStrategy} from "@angular/common";

@NgModule({
  /* Khai báo các component, pipe, directive */
  declarations: [
    AppComponent,
    CartComponent,
    CheckoutComponent,
    FooterComponent,
    ForgotPasswordComponent,
    HeaderComponent,
    HomeComponent,
    LoginComponent,
    ProductsingleComponent,
    ProfileDetailsComponent,
    ShopComponent,
    SignupComponent
  ],
  /* Nạp các module khác */
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, /* Dùng để thao tác với Template-driven forms*/
    ReactiveFormsModule, /* Dùng để thao tác với Reactive Forms,
    phương pháp này tránh việc sử dụng ngModel, required, ... thay vào đó là tạo các Object Model */
    HttpClientModule, /* Dùng để thực hiện các Http dễ dàng custom các request option */
    BrowserAnimationsModule, /* Dùng để sử dụng các Animation tương tác giữa các thẻ html */
    SlickCarouselModule, /* Cho phép tạo responsive carousel đẹp mắt hơn */
    CarouselModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right',
      timeOut: 2000
    })
  ],
  /* Các service mà các component có thể sử dụng */
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy},],
  /* Định nghĩa component chính của module */
  bootstrap: [AppComponent]
})
export class AppModule {
}
