import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable, never} from 'rxjs';
import {Injectable} from '@angular/core';
import {NotiService} from './notification.service';
import {environment} from '../../../../environments/environment';
import {GridParam} from '../model/grid-param';
import {BasicService} from '../utils/basic.service';
import {HelperService} from '../utils/helper.service';
import {CommonServiceService} from '../utils/common-service.service';
import {SearchReport} from '../model/searchReport';
import {TranslateService} from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})

export class DataPackageService extends BasicService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;
  public loading = new BehaviorSubject<any>('next')

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Credentials': 'true'
    })
  }

  // Kiểu chọn
  public type = new BehaviorSubject<any>('');

  // Năm đc chọn trên header
  public yearCurrent = new BehaviorSubject<any>('');

  // Tháng đc chọn trên header
  public monthCurrent = new BehaviorSubject<any>('');

  // Quý đc chọn trên header
  public quartersCurrent = new BehaviorSubject<any>('');

  public subheaderObj = new BehaviorSubject<any>({});

  constructor(private http: HttpClient, private notiService: NotiService, private commonService: CommonServiceService,
              private translateSerive: TranslateService,
              public helperService: HelperService) {
    super(http, helperService);
  }

  export(data) {
    const url = this.API + `data_packages/exportExcel`;
    return this.commonService.downloadFile(url, data, null, this.translateSerive.instant('PACKAGE_MANAGEMENT.EXPORT_FILE_NAME') + `.xlsx`);
  }

  delete(data) {
    return this.httpClient.post<any>(`${this.API}data_packages/delete`, data, this.httpOptions);
  }
  create(data) {
    return this.http.post<any>(`${this.API}data_packages/create`, data, this.httpOptions);
  }

  update(data) {
    return this.http.post<any>(`${this.API}data_packages/update`, data, this.httpOptions);
  }

  report(search: SearchReport): Observable<any> {
    return this.http.post<any>(`${this.API}packageStatistics/search`, search);
  }
  // Năm
  changeYearCurrent(value: any) {
    this.yearCurrent.next(value);
  }

  changeMonthCurrent(value: any) {
    this.monthCurrent.next(value);
  }

  changeQuartersCurrent(value: any) {
    this.quartersCurrent.next(value);
  }

  changeSubheader(value: any) {
    this.subheaderObj.next(value);
  }
}
