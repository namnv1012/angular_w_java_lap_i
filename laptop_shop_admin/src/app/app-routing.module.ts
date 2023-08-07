import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {BaseComponent} from './views/theme/base/base.component';
import {AuthGuard} from './core/auth';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () =>
      import('./views/pages/auth/auth.module').then((m) => m.AuthModule),
  },
  {
    path: 'error',
    loadChildren: () =>
      import('./views/pages/error/error.module').then((m) => m.ErrorModule),
  },
  {
    path: '',
    component: BaseComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'builder',
        loadChildren: () =>
          import('./views/theme/content/builder/builder.module').then(
            (m) => m.BuilderModule
          ),
      },
      {
        path: 'system',
        loadChildren: () =>
          import('./views/pages/system/system.module').then(
            (m) => m.SystemModule
          ),
      },
      {path: '', redirectTo: '/system/dashboard', pathMatch: 'full'},
      {path: '**', redirectTo: '/system/dashboard', pathMatch: 'full'},
    ],
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
