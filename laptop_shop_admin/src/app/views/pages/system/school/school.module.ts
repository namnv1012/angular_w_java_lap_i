import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SchoolComponent} from './school.component';
import {RouterModule, Routes} from '@angular/router';
import {ModalModule} from 'ngx-bootstrap/modal';
import {NgSelectModule} from '@ng-select/ng-select';
import {AngularFileUploaderModule} from 'angular-file-uploader';
import {AgGridModule} from 'ag-grid-angular';
import {ActionShoolComponent} from './action-shool/action-shool.component';
import {FormsModule} from '@angular/forms';
import {ConfigurationSchoolComponent} from './configuration-school/configuration-school.component';
import 'ag-grid-enterprise';
// @ts-ignore
import {PositionComponent} from './position/position.component';
import {ToastrModule} from 'ngx-toastr';
import {NzTreeSelectModule} from 'ng-zorro-antd';

const routes: Routes = [
  {
    path: '',
    component: SchoolComponent,
  },
  {
    path: 'configuration-school',
    component: ConfigurationSchoolComponent,
  },
  {path: '', redirectTo: 'school', pathMatch: 'full'},
  {path: '**', redirectTo: 'school', pathMatch: 'full'},
]


@NgModule({
  // tslint:disable-next-line:max-line-length
  declarations: [SchoolComponent, ActionShoolComponent, ConfigurationSchoolComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    NgSelectModule,
    FormsModule,
    AngularFileUploaderModule,
    AgGridModule.withComponents([]),
    ModalModule.forRoot(),
    ToastrModule.forRoot(),
    AgGridModule.withComponents([]),
    NzTreeSelectModule,
  ],
  entryComponents:
    [
      ActionShoolComponent,
      PositionComponent
    ]
})
export class SchoolModule {
}
