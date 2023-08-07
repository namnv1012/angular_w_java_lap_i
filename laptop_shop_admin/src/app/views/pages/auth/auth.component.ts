import {ChangeDetectorRef, Component, ElementRef, OnInit, Renderer2, ViewEncapsulation} from '@angular/core';
import {LayoutConfigService, SplashScreenService, TranslationService} from '../../../core/_base/layout';
// Auth
import {NotiService} from '../../../core/service/service-model/notification.service';
import {Subject} from 'rxjs';
import {TranslateService} from '@ngx-translate/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AuthComponent implements OnInit {
  today: number = Date.now();
  headerLogo: string;
  schoolInfo: any;
  private unsubscribe: Subject<any>;

  constructor(
    private el: ElementRef,
    private render: Renderer2,
    private layoutConfigService: LayoutConfigService,
    private translationService: TranslationService,
    private translate: TranslateService,
    private notiService: NotiService,
    private cdr: ChangeDetectorRef,
    private splashScreenService: SplashScreenService,
  ) {
    this.unsubscribe = new Subject();
  }

  ngOnInit(): void {
    this.headerLogo = this.layoutConfigService.getLogo();
    this.splashScreenService.hide();
  }
}
