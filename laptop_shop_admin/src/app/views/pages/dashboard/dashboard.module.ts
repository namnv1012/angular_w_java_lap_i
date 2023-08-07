import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {NgbDropdownModule, NgbTabsetModule, NgbTooltipModule} from '@ng-bootstrap/ng-bootstrap';
// Core Module
import {CoreModule} from '../../../core/core.module';
import {PartialsModule} from '../../partials/partials.module';
import {DashboardComponent} from '../system/dashboard/dashboard.component';

@NgModule({
  imports: [
    CommonModule,
    PartialsModule,
    CoreModule,
    RouterModule.forChild([
      {
        path: '',
        component: DashboardComponent
      },
    ]),
    NgbDropdownModule,
    NgbTabsetModule,
    NgbTooltipModule,
  ],
  providers: [],
  declarations: [
    DashboardComponent,
  ]
})
export class DashboardModule {
}
