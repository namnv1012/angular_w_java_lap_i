import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
// Material
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
// Translate
import {TranslateModule} from '@ngx-translate/core';
// NGRX
// CRUD
import {InterceptService} from '../../../core/_base/crud/';
// Module components
import {AuthComponent} from './auth.component';
import {LoginComponent} from './login/login.component';
import {ForgotPasswordComponent} from './forgot-password/forgot-password.component';
import {AuthNoticeComponent} from './auth-notice/auth-notice.component';
// Auth
import {AuthGuard, AuthService} from '../../../core/auth';
import {DropDownsModule} from '@progress/kendo-angular-dropdowns';
import {NotificationModule} from '@progress/kendo-angular-notification';
import {PartialsModule} from '../../partials/partials.module';

const routes: Routes = [
  {
    path: '',
    component: AuthComponent,
    children: [
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
      },
      {
        path: 'login',
        component: LoginComponent,
        data: {returnUrl: window.location.pathname}
      },
      {
        path: 'forgot-password',
        component: ForgotPasswordComponent,
      }
    ]
  }
];


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    RouterModule.forChild(routes),
    MatInputModule,
    MatFormFieldModule,
    MatCheckboxModule,
    TranslateModule.forChild(),
    DropDownsModule,
    NotificationModule,
    PartialsModule
  ],
  providers: [
    InterceptService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptService,
      multi: true
    },
  ],
  exports: [AuthComponent],
  declarations: [
    AuthComponent,
    LoginComponent,
    ForgotPasswordComponent,
    AuthNoticeComponent
  ]
})

export class AuthModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: AuthModule,
      providers: [
        AuthService,
        AuthGuard
      ]
    };
  }
}
