import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {AuthLayoutRoutes} from './auth-layout.routing';
import {MatDialogModule} from '@angular/material/dialog';
import {LoginComponent} from '../../views/pages/auth/login/login.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AuthLayoutRoutes),
    FormsModule,
    MatDialogModule
  ],
  declarations: [
    LoginComponent,
  ]
})
export class AuthLayoutModule {
}
