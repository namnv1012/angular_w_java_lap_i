import {CommonModule, DatePipe} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {TranslateModule} from '@ngx-translate/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {OverlayModule} from '@angular/cdk/overlay';
import {PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface} from 'ngx-perfect-scrollbar';
import {InlineSVGModule} from 'ng-inline-svg';
import 'hammerjs';
import {NgxPermissionsModule} from 'ngx-permissions';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {CoreModule} from './core/core.module';
import {ThemeModule} from './views/theme/theme.module';
import {PartialsModule} from './views/partials/partials.module';
import {
  DataTableService,
  KtDialogService,
  LayoutConfigService,
  LayoutRefService,
  MenuAsideService,
  MenuConfigService,
  MenuHorizontalService,
  PageConfigService,
  SplashScreenService,
  SubheaderService
} from './core/_base/layout';
import {AuthGuard, AuthNoticeService, AuthService} from './core/auth';
import {HttpUtilsService, InterceptService, TypesUtilsService} from './core/_base/crud';
import {LayoutConfig} from './core/_config/layout.config';
import {HIGHLIGHT_OPTIONS, HighlightModule} from 'ngx-highlightjs';
import xml from 'highlight.js/lib/languages/xml';
import json from 'highlight.js/lib/languages/json';
import scss from 'highlight.js/lib/languages/scss';
import typescript from 'highlight.js/lib/languages/typescript';
import {GridModule} from '@progress/kendo-angular-grid';
import {OAuthModule} from 'angular-oauth2-oidc';
import {TreeViewModule} from '@progress/kendo-angular-treeview';
import {NotificationModule} from '@progress/kendo-angular-notification';
import {InputsModule} from '@progress/kendo-angular-inputs';
import {AngularFileUploaderModule} from 'angular-file-uploader';
import {ToastrModule} from 'ngx-toastr';
import {SecurityService} from './core/auth/_services/security.service';
import { ChartModule } from '@progress/kendo-angular-charts';
// tslint:disable-next-line:class-name
const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  wheelSpeed: 0.5,
  swipeEasing: true,
  minScrollbarLength: 40,
  maxScrollbarLength: 300
};

export function initializeLayoutConfig(appConfig: LayoutConfigService) {
  return () => {
    if (appConfig.getConfig() === null) {
      appConfig.loadConfigs(new LayoutConfig().configs);
    }
  };
}

export function getHighlightLanguages() {
  return [
    {name: 'typescript', func: typescript},
    {name: 'scss', func: scss},
    {name: 'xml', func: xml},
    {name: 'json', func: json}
  ];
}

@NgModule({
  declarations: [AppComponent],
  exports: [],
  providers: [
    AuthService,
    LayoutConfigService,
    LayoutRefService,
    MenuConfigService,
    PageConfigService,
    KtDialogService,
    DataTableService,
    SplashScreenService,
    AuthGuard,
    SecurityService,
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    },
    {
      // layout config initializer
      provide: APP_INITIALIZER,
      useFactory: initializeLayoutConfig,
      deps: [LayoutConfigService],
      multi: true
    },
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        languages: getHighlightLanguages
      }
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptService,
      multi: true,
    },
    // template services
    SubheaderService,
    MenuHorizontalService,
    MenuAsideService,
    HttpUtilsService,
    TypesUtilsService,
    AuthNoticeService,
    DatePipe
  ],
  bootstrap: [AppComponent],
  imports: [
    BrowserAnimationsModule,
    AngularFileUploaderModule,
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgxPermissionsModule.forRoot(),
    HighlightModule,
    PartialsModule,
    CoreModule,
    OverlayModule,
    TranslateModule.forRoot(),
    InlineSVGModule.forRoot(),
    ThemeModule,
    GridModule,
    OAuthModule.forRoot(),
    TreeViewModule,
    NotificationModule,
    InputsModule,
    ChartModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right',
      timeOut: 2000
    }),
  ]
})
export class AppModule {
}
