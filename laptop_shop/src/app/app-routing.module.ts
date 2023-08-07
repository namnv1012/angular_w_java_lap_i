import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./component/home/home.component";
import {ProductsingleComponent} from "./component/productsingle/productsingle.component";
import {CartComponent} from "./component/cart/cart.component";
import {CheckoutComponent} from "./component/checkout/checkout.component";
import {ShopComponent} from "./component/shop/shop.component";
import {LoginComponent} from "./component/login/login.component";
import {SignupComponent} from "./component/signup/signup.component";
import {ForgotPasswordComponent} from "./component/forgot-password/forgot-password.component";
import {ProfileDetailsComponent} from "./component/profile-details/profile-details.component";

const routes: Routes = [
  {path: "", component: HomeComponent},
  {path: "product", component: ProductsingleComponent},
  {path: "cart", component: CartComponent},
  {path: "checkout", component: CheckoutComponent},
  {path: "shop", component: ShopComponent},
  {path: "login", component: LoginComponent},
  {path: "signup", component: SignupComponent},
  {path: "forgot-password", component: ForgotPasswordComponent},
  {path: "profile-details", component: ProfileDetailsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
