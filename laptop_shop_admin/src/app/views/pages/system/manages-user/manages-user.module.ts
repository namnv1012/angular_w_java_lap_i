import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NgSelectModule} from '@ng-select/ng-select';
import {FormsModule} from '@angular/forms';
import {AngularFileUploaderModule} from 'angular-file-uploader';
import {AgGridModule} from 'ag-grid-angular';
import {ModalModule} from 'ngx-bootstrap/modal';
import {ManagesUserComponent} from './manages-user.component';
import {TranslateModule} from '@ngx-translate/core';
import {CreateUpdateUserComponent} from './create-update-user/create-update-user.component';
import {ImportFileComponent} from './import-file/import-file.component';

const routes: Routes = [
  {
    path: '',
    component: ManagesUserComponent,
  },
]

@NgModule({
  declarations: [CreateUpdateUserComponent, ImportFileComponent],
  imports: [
    CommonModule,
    CommonModule,
    NgModule,
    RouterModule.forChild(routes),
    NgSelectModule,
    FormsModule,
    AngularFileUploaderModule,
    AgGridModule.withComponents([]),
    ModalModule.forRoot(),
    AgGridModule.withComponents([]),
    TranslateModule,
  ],
  entryComponents:
    []
})
export class ManagesSchoolModule {
}
