import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';

@Injectable({
  providedIn: 'root',
})
export class HelperService {
  public APP_TOAST_MESSAGE = new BehaviorSubject<any>([]);
  public APP_CONFIRM_DELETE = new BehaviorSubject(null);
  public APP_SHOW_PROCESSING = new BehaviorSubject<any>([]);

  constructor(
    private toastr: ToastrService,
    private translateService: TranslateService) {
  }

  isProcessing(isProcessing: boolean) {
    this.APP_SHOW_PROCESSING.next(isProcessing);
  }

  confirmDelete(data) {
    this.APP_CONFIRM_DELETE.next(data);
    // this.APP_CONFIRM_DELETE.pipe(data);
  }

  showErrors(message: string) {
    this.toastr.error(message);
  }

  showUnknowErrors() {
    this.toastr.error(this.translateService.instant('common.notify.fail'));
  }
}
