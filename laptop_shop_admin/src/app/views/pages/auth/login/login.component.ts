import {
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable, Subject, timer} from 'rxjs';
import {finalize, scan, takeUntil, takeWhile, tap} from 'rxjs/operators';
import {TranslateService} from '@ngx-translate/core';
import {Select, Store} from '@ngxs/store';
import {AuthNoticeService, AuthService} from '../../../../core/auth';
import {TenantState} from '../../../../core/service/states/tenant.state';
import {GetAllTenant} from '../../../../core/service/actions/tenant.action';
import {NotiService} from '../../../../core/service/service-model/notification.service';
import {CommonResponseModel} from '../../../../core/service/model/common-response.model';
import {BsModalRef, BsModalService, ModalDirective} from 'ngx-bootstrap/modal';
import {SchoolServices} from '../../system/school/school.service';
import {StorageSessionService} from '../../../../core/auth/_services/storage.session.service';

const DEMO_PARAMS = {
  PHONE: '0123456789',
  PASSWORD: '123456aA@'
};

interface TenantItem {
  name: string;
  id: string;
}


@Component({
  // tslint:disable-next-line:component-selector
  selector: 'kt-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit, OnDestroy {
  counter: any = 120;
  threeLastNumber: any;
  otpInput: any;
  numberOtpInput = 0;
  loginForm: FormGroup;
  otpForm: FormGroup;
  verifyOTPForm: FormGroup;
  loading = false;
  loadingOtp = false;
  loadingVerifyOtp = false;
  otp: any;
  passwordReset: any;
  errors: any = [];
  accountName: string;
  timer$: any
  componentDestroyed$: Subject<boolean> = new Subject()
  verifySuccess = false;
  private unsubscribe: Subject<any>;
  info: any
  private returnUrl: any;

  @Select(TenantState.getAllTenant) tenants: Observable<CommonResponseModel[]>
  source: Array<TenantItem>;
  data: Array<TenantItem>;
  toggle1: any = false;
  toggle2: any = false;
  modalRef: BsModalRef;
  @ViewChild('newUnit') public newUnit: ModalDirective;

  constructor(
    private router: Router,
    private auth: AuthService,
    private authNoticeService: AuthNoticeService,
    private translate: TranslateService,
    private store: Store,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute,
    private notiService: NotiService,
    private modalService: BsModalService,
    private schoolServices: SchoolServices,
    private storageSessionService: StorageSessionService,
  ) {
    this.unsubscribe = new Subject();
  }

  ngOnInit(): void {
    this.otp = '';
    this.passwordReset = '';
    this.accountName = '';
    this.store.dispatch(new GetAllTenant());
    this.tenants.subscribe((tenant) => {
      if (typeof tenant !== 'undefined') {
        this.source = tenant.map(value => {
          return {name: value.name, id: value.id}
        });
        this.data = this.source.slice();
      }
    })

    this.initLoginForm();
    // redirect back to the returnUrl before login
    this.route.queryParams.subscribe(params => {
      this.returnUrl = params.returnUrl || '/system/dashboard';
    });
  }

  ngOnDestroy(): void {
    this.authNoticeService.setNotice(null);
    this.unsubscribe.next();
    this.unsubscribe.complete();
    this.loading = false;
    this.loadingOtp = false;
    this.loadingVerifyOtp = false;
    this.timer$ = null;
    this.componentDestroyed$.next(true)
    this.componentDestroyed$.complete()
  }

  initLoginForm() {
    // demo message to show
    if (!this.authNoticeService.onNoticeChanged$.getValue()) {
      const initialNotice = `Use account
			<strong>${DEMO_PARAMS.PHONE}</strong> and password
			<strong>${DEMO_PARAMS.PASSWORD}</strong> to continue.`;
      this.authNoticeService.setNotice(initialNotice, 'info');
    }

    this.loginForm = this.fb.group({
      phone: new FormControl('', [Validators.maxLength(50)]),
      password: new FormControl('', [Validators.maxLength(20)]),

    });

    this.otpForm = this.fb.group({
      accountName: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    });

    this.verifyOTPForm = this.fb.group({
      otpNumber1: new FormControl('', [Validators.maxLength(1), Validators.pattern(/^-?([0-9]\d*)?$/)]),
      otpNumber2: new FormControl('', [Validators.maxLength(1), Validators.pattern(/^-?([0-9]\d*)?$/)]),
      otpNumber3: new FormControl('', [Validators.maxLength(1), Validators.pattern(/^-?([0-9]\d*)?$/)]),
      otpNumber4: new FormControl('', [Validators.maxLength(1), Validators.pattern(/^-?([0-9]\d*)?$/)]),
      otpNumber5: new FormControl('', [Validators.maxLength(1), Validators.pattern(/^-?([0-9]\d*)?$/)]),
      otpNumber6: new FormControl('', [Validators.maxLength(1), Validators.pattern(/^-?([0-9]\d*)?$/)]),
    });

  }

  get f() {
    return this.loginForm;
  }

  get f1() {
    return this.otpForm;
  }

  get f2() {
    return this.verifyOTPForm;
  }

  onBlurEvent(event: any) {
    const data = event.target.value.replace(/\s/g, '')
    this.loginForm.patchValue({
      phone: data,
    });
    if (data === '') {
      return this.loginForm.controls.phone.setErrors({isNullUser: true})
    }
    return null;
  }

  submit() {
    const controls = this.loginForm.controls;
    /** check form */
    if (this.loginForm.invalid) {
      Object.keys(controls).forEach(controlName =>
        controls[controlName].markAsTouched()
      );
      return;
    }

    this.loading = true;

    const authData = {
      phone: controls.phone.value,
      password: controls.password.value
    };

    this.auth.login(authData.phone.trim(), authData.password.trim())
      .pipe(
        tap(user => {
          if (user) {
            this.router.navigate([this.returnUrl])
            this.notiService.showNoti(this.translate.instant('AUTH.LOGIN.MESSAGE.SUCCESS'), 'success');
            this.loading = false;
          } else {
            this.notiService.showNoti(this.translate.instant('AUTH.LOGIN.MESSAGE.FAILURE'), 'warning');
            this.loading = false;
          }
        }, error => {
          if (error.error.status === undefined || error.error.status === null) {
            this.notiService.showNoti(this.translate.instant('SYSTEM.CONNECT_FAILURE'), 'error');
          } else {
            this.notiService.showNoti('Tài khoản và mật khẩu không chính xác!', 'error');
          }

        }),
        takeUntil(this.unsubscribe),
        finalize(() => {
          this.loading = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe();
    this.cdr.detectChanges();
  }

  changeType(type, num) {
    if (type.type === 'password') {
      type.type = 'text';
    } else {
      type.type = 'password';
    }

    if (num === 1)
      this.toggle1 = !this.toggle1;
    else
      this.toggle2 = !this.toggle2;
  }

  isVietnamesePhoneNumber(phoneNumber) {
    return /([\+84|84|0]+(3|5|7|8|9|1[2|6|8|9]))+([0-9]{8})\b/.test(phoneNumber);
  }

  openModal(template: TemplateRef<any>) {
    this.timer$ = null;
    this.verifyOTPForm.reset()
    this.otpForm.reset()
    this.resetValue()
    this.modalRef = this.modalService.show(
      template,
      Object.assign({}, {class: 'addnew-unit-md modal-dialog-custom'})
    );
  }

  onBlurEventOTP(event: any) {
    this.otpForm.patchValue({
      accountName: event.target.value.replace(/\s/g, ''),
    });
  }

  onSubmitOtp() {
    const controls = this.otpForm.controls;
    if (this.otpForm.invalid) {
      Object.keys(controls).forEach(controlName =>
        controls[controlName].markAsTouched()
      );
      return;
    }
    this.accountName = controls.accountName.value;
    this.loadingOtp = true;
    this.auth.requestOTP(this.accountName).pipe(
      tap(response => {
        if (response) {
          const body = response.body;
          this.threeLastNumber = body.phoneNumber.substr(body.phoneNumber.length - 3)
          this.timer$ = null;
          this.componentDestroyed$.next(true)
          this.componentDestroyed$.complete()
          console.log('data otp ===>', body.resetKey)
          this.otp = body.resetKey;
          this.notiService.showNoti(this.translate.instant('AUTH.OTP.REQUEST_SUCCESS'), 'success');
          this.loadingOtp = false;
          this.resetCount()
          this.otpInput = ''
          this.timer$ = timer(0, 1000).pipe(
            takeUntil(this.componentDestroyed$),
            scan(acc => --this.counter, this.counter),
            takeWhile(x => x >= 0)
          ).subscribe(() => this.cdr.detectChanges());
        } else {
          this.notiService.showNoti(this.translate.instant('AUTH.OTP.REQUEST_FAILURE'), 'warning');
          this.loadingOtp = false;
        }
      }, error => {
        this.notiService.showNoti(error.error.detail, 'error');
      }),
      takeUntil(this.unsubscribe),
      finalize(() => {
        this.loadingOtp = false;
        this.cdr.markForCheck();
      })
    ).subscribe();
    this.cdr.detectChanges();
  }

  verifyOtp() {
    const controls = this.verifyOTPForm.controls;
    if (this.verifyOTPForm.invalid) {
      Object.keys(controls).forEach(controlName =>
        controls[controlName].markAsTouched()
      );
      return;
    }
    if (controls.otpNumber1.value && controls.otpNumber2.value && controls.otpNumber3.value &&
      controls.otpNumber4.value && controls.otpNumber5.value && controls.otpNumber6.value) {
      this.otpInput = controls.otpNumber1.value + '' + controls.otpNumber2.value + '' + controls.otpNumber3.value
        + '' + controls.otpNumber4.value + '' + controls.otpNumber5.value + '' + controls.otpNumber6.value;
    } else {
      return
    }

    this.loadingVerifyOtp = true;
    this.auth.verifyOTP(this.accountName, this.otpInput).pipe(
      tap(response => {
        if (response) {
          console.log('data passwordReset ===>', response.body)
          this.passwordReset = response.body;
          this.notiService.showNoti(this.translate.instant('AUTH.FORGOT.SUCCESS'), 'success');
          this.loadingVerifyOtp = false;
          this.removeCount()
          this.verifySuccess = true;
        } else {
          this.notiService.showNoti(this.translate.instant('AUTH.OTP.FAILURE'), 'warning');
          this.loadingVerifyOtp = false;
        }
      }, error => {
        this.notiService.showNoti(error.error.detail, 'error');
      }),
      takeUntil(this.unsubscribe),
      finalize(() => {
        this.loadingVerifyOtp = false;
        this.cdr.markForCheck();
      })
    ).subscribe();
    this.cdr.detectChanges();
  }

  onCancelOtp() {
    this.modalRef.hide()
    this.resetValue();
  }

  resetValue() {
    this.otp = ''
    this.passwordReset = ''
    this.verifySuccess = false;
    this.numberOtpInput = 0;
  }

  resetCount() {
    this.counter = 120
  }

  removeCount() {
    this.counter = null
  }

  restart() {
    console.log(this.counter)
  }

  onDigitInput(event) {
    let element;
    const regex = /^-?([0-9]\d*)?$/;
    if (event.code !== 'Backspace') {
      if (event.key.trim() !== '' && event.key.trim().match(regex)) {
        this.numberOtpInput < 6 ? this.numberOtpInput++ : this.numberOtpInput = 6
        element = event.srcElement.nextElementSibling;
      } else {
        element = null
      }
    }
    if (event.code === 'Backspace') {
      this.numberOtpInput > 0 ? this.numberOtpInput-- : this.numberOtpInput = 0
      element = event.srcElement.previousElementSibling;
    }
    if (element == null)
      return;
    else
      element.focus();
  }

  replaceSpacePassword(event: any) {
    const data = event.target.value.replace(/\s/g, '')
    this.loginForm.patchValue({
      password: data,
    });
    if (data === '') {
      event.target.type = 'password';
      this.toggle1 = false;
      return this.loginForm.controls.password.setErrors({isNull: true})
    }
    return null;
  }

  replaceSpaceUserName(event: any) {
    const data = event.target.value.replace(/\s/g, '')
    this.loginForm.patchValue({
      phone: data,
    });
    if (data === '') {
      return this.loginForm.controls.phone.setErrors({isNullUser: true})
    }
    return null;
  }
}
