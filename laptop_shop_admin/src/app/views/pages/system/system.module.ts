import {NgModule} from '@angular/core';

import {RouterModule, Routes} from '@angular/router';
import {SystemComponent} from './system.component';
import {CommonModule} from '@angular/common';
import {PanelBarModule, TabStripModule} from '@progress/kendo-angular-layout';
import {ButtonsModule} from '@progress/kendo-angular-buttons';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbCollapseModule, NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {DialogModule} from '@progress/kendo-angular-dialog';
import {
  DropDownListModule,
  DropDownsModule,
} from '@progress/kendo-angular-dropdowns';
import {
  FormFieldModule,
  InputsModule,
  TextBoxModule,
} from '@progress/kendo-angular-inputs';
import {LayoutModule} from '@angular/cdk/layout';
import {LabelModule} from '@progress/kendo-angular-label';
import {HttpClientModule} from '@angular/common/http';
import {NgxsModule} from '@ngxs/store';
import {DateInputsModule} from '@progress/kendo-angular-dateinputs';
import {environment} from '../../../../environments/environment';
import {
  BodyModule,
  ColumnResizingService,
  FilterMenuModule,
  GridModule,
  PagerModule,
  SharedModule,
} from '@progress/kendo-angular-grid';
import {MatSelectModule} from '@angular/material/select';
import {ActionShoolComponent} from './school/action-shool/action-shool.component';
import {SchoolComponent} from './school/school.component';
import {NgSelectModule} from '@ng-select/ng-select';
import {AngularFileUploaderModule} from 'angular-file-uploader';
import {AgGridModule} from 'ag-grid-angular';
import {ModalModule} from 'ngx-bootstrap/modal';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatOptionModule} from '@angular/material/core';
import {MatInputModule} from '@angular/material/input';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatRadioModule} from '@angular/material/radio';
import 'ag-grid-enterprise';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NzTreeSelectModule} from 'ng-zorro-antd/tree-select';
import {TranslateModule} from '@ngx-translate/core';
// tslint:disable-next-line:max-line-length
import {SystemsDirective} from './systems.directive';
import {CoreModule} from '../../../core/core.module';
import {MatTooltipModule} from '@angular/material/tooltip';
import {TreeviewModule} from 'ngx-treeview';
import {EditorModule} from '@progress/kendo-angular-editor';
import {ChangePasswordComponent} from '../auth/change-password/change-password.component';
import {ChartModule} from '@progress/kendo-angular-charts';
import {DashboardComponent} from './dashboard/dashboard.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatIconModule} from '@angular/material/icon';
import {ManagesUserComponent} from './manages-user/manages-user.component';
import {CreateUpdateUserComponent} from './manages-user/create-update-user/create-update-user.component';
import {ImportFileComponent} from './manages-user/import-file/import-file.component';
import {PopupConfirmComponent} from './popup-confirm/popup-confirm.component';
import {FormatTimePipe} from '../../../core/service/utils/format-time.pipe';
import {ManagesCategoryComponent} from './manages-category/manages-category.component';
import {ManagesProducerComponent} from './manages-producer/manages-producer.component';
import {ManagesProductComponent} from './manages-product/manages-product.component';
import {ManagesOrdersComponent} from './manages-orders/manages-orders.component';
import {
  CreateUpdateCategoryComponent
} from './manages-category/create-update-category/create-update-category.component';
import {
  ActionManagesCategoryComponent
} from './manages-category/action-manages-category/action-manages-category.component';
import {
  CreateUpdateProducerComponent
} from './manages-producer/create-update-producer/create-update-producer.component';
import {
  ActionManagesProducerComponent
} from './manages-producer/action-manages-producer/action-manages-producer.component';
import {ActionManagesProductComponent} from './manages-product/action-manages-product/action-manages-product.component';
import {CreateUpdateProductComponent} from './manages-product/create-update-product/create-update-product.component';
import {ActionDetailOrderComponent} from './manages-orders/action-detail-order/action-detail-order.component';
import {ViewDetailOrderComponent} from './manages-orders/view-detail-order/view-detail-order.component';
import {DetailOrdersComponent} from './manages-orders/detail-orders/detail-orders.component';
import {CellViewImgProductComponent} from './manages-product/cell-view-img-product/cell-view-img-product.component';
import {
  ImportQuantityProductComponent
} from './manages-product/import-quantity-product/import-quantity-product.component';
import {
  DashboardQuantityProductComponent
} from './manages-product/dashboard-quantity-product/dashboard-quantity-product.component';
import {ManagesWarrantyComponent} from './manages-warranty/manages-warranty.component';
import {
  ViewWarrantyCustomerComponent
} from './manages-warranty/view-warranty-customer/view-warranty-customer.component';
import {
  ActionManagesWarrantyComponent
} from './manages-warranty/action-manages-warranty/action-manages-warranty.component';
import {
  CreateWarrantyCustomerComponent
} from './manages-warranty/create-warranty-customer/create-warranty-customer.component';
import {ListWarrantyDetailsComponent} from './manages-warranty/list-warranty-details/list-warranty-details.component';

const routes: Routes = [
  {
    path: '',
    component: SystemComponent,
  },
  {
    path: 'account/change-password',
    component: ChangePasswordComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  {
    path: 'manages-user',
    component: ManagesUserComponent,
  },
  {
    path: 'manages-category',
    component: ManagesCategoryComponent,
  },
  {
    path: 'manages-producer',
    component: ManagesProducerComponent,
  },
  {
    path: 'manages-product',
    component: ManagesProductComponent,
  },
  {
    path: 'manages-orders',
    component: ManagesOrdersComponent,
  },
  {
    path: 'order-detail',
    component: DetailOrdersComponent,
  },
  {
    path: 'create-update-product',
    component: CreateUpdateProductComponent,
  },
  {
    path: 'dashboard-quantity-product',
    component: DashboardQuantityProductComponent,
  },
  {
    path: 'manages-warranty',
    component: ManagesWarrantyComponent,
  },
  {
    path: 'list-warranty-details',
    component: ListWarrantyDetailsComponent,
  },
  {path: '', redirectTo: 'system', pathMatch: 'full'},
  {path: '**', redirectTo: 'system', pathMatch: 'full'},
];

// @ts-ignore
// @ts-ignore
// @ts-ignore
@NgModule({
  providers: [ColumnResizingService,],
  declarations: [
    SystemComponent,
    SchoolComponent,
    SchoolComponent,
    ActionShoolComponent,
    SchoolComponent,
    SystemsDirective,
    ChangePasswordComponent,
    DashboardComponent,
    ManagesUserComponent,
    CreateUpdateUserComponent,
    ImportFileComponent,
    PopupConfirmComponent,
    FormatTimePipe,
    ManagesCategoryComponent,
    ManagesProducerComponent,
    ManagesProductComponent,
    ManagesOrdersComponent,
    CreateUpdateCategoryComponent,
    ActionManagesCategoryComponent,
    CreateUpdateProducerComponent,
    ActionManagesProducerComponent,
    ActionManagesProductComponent,
    CreateUpdateProductComponent,
    ActionDetailOrderComponent,
    ViewDetailOrderComponent,
    DetailOrdersComponent,
    CellViewImgProductComponent,
    ImportQuantityProductComponent,
    DashboardQuantityProductComponent,
    ManagesWarrantyComponent,
    ViewWarrantyCustomerComponent,
    ActionManagesWarrantyComponent,
    CreateWarrantyCustomerComponent,
    ListWarrantyDetailsComponent,
  ],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    InputsModule,
    LabelModule,
    FilterMenuModule,
    NgbModule,
    InputsModule,
    LabelModule,
    FilterMenuModule,
    HttpClientModule,
    NgSelectModule,
    NzTreeSelectModule,
    AngularFileUploaderModule,
    ModalModule.forRoot(),
    NgxsModule.forRoot([], {
      developmentMode: !environment.production,
    }),
    FormsModule,
    NgbModalModule,
    NgbCollapseModule,
    TabStripModule,
    PanelBarModule,
    LayoutModule,
    GridModule,
    ButtonsModule,
    DropDownsModule,
    DateInputsModule,
    SharedModule,
    DialogModule,
    DropDownListModule,
    FormFieldModule,
    ReactiveFormsModule,
    TextBoxModule,
    BodyModule,
    PagerModule,
    MatFormFieldModule,
    MatOptionModule,
    MatSelectModule,
    MatInputModule,
    MatCheckboxModule,
    MatPaginatorModule,
    MatAutocompleteModule,
    MatRadioModule,
    TranslateModule,
    CoreModule,
    MatTooltipModule,
    EditorModule,
    AngularFileUploaderModule,
    TreeviewModule.forRoot(),
    ChartModule,
    MatDatepickerModule,
    MatIconModule,
    AgGridModule
  ],
  entryComponents: [
    ActionShoolComponent,
    ChangePasswordComponent,
    CreateUpdateUserComponent,
    ImportFileComponent,
    PopupConfirmComponent,
    CreateUpdateCategoryComponent,
    ActionManagesCategoryComponent,
    CreateUpdateProducerComponent,
    ActionManagesProducerComponent,
    ActionDetailOrderComponent,
    CreateUpdateProductComponent,
    CellViewImgProductComponent,
    ActionManagesProductComponent,
    ImportQuantityProductComponent,
    ViewWarrantyCustomerComponent,
    ActionManagesWarrantyComponent,
    CreateWarrantyCustomerComponent,
  ],
})

export class SystemModule {
}
