import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {PartialsModule} from '../partials/partials.module';
import {CoreModule} from '../../core/core.module';
import {NgbCollapseModule} from '@ng-bootstrap/ng-bootstrap';
import {DropDownsModule} from '@progress/kendo-angular-dropdowns';
import {BodyModule, GridModule, PagerModule, SharedModule} from '@progress/kendo-angular-grid';
import {ButtonModule} from '@progress/kendo-angular-buttons';
import {DialogModule} from '@progress/kendo-angular-dialog';
import {SystemModule} from './system/system.module';
import {AgGridModule} from 'ag-grid-angular';
import {MatSelectModule} from '@angular/material/select';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatPaginatorModule} from '@angular/material/paginator';
import {NgSelectModule} from '@ng-select/ng-select';
import {MatTooltipModule} from '@angular/material/tooltip';

@NgModule({
  declarations: [],
  exports: [],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    CoreModule,
    PartialsModule,
    NgbCollapseModule,
    DropDownsModule,
    GridModule,
    ButtonModule,
    PagerModule,
    DialogModule,
    SystemModule,
    SharedModule,
    BodyModule,
    AgGridModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatPaginatorModule,
    NgSelectModule,
    MatTooltipModule,
  ],
  providers: []
})
export class PagesModule {
}
