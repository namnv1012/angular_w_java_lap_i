import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {NotiService} from '../../../../core/service/service-model/notification.service';
import {CommonServiceService} from '../../../../core/service/utils/common-service.service';
import {HelperService} from '../../../../core/service/utils/helper.service';
import {BasicService} from '../../../../core/service/utils/basic.service';
import {environment} from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SchoolServices extends BasicService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;
  public loading = new BehaviorSubject<any>('next')
  public sideBar = new BehaviorSubject<any>(false)

  constructor(private http: HttpClient, private notiService: NotiService, private commonService: CommonServiceService,
              public helperService: HelperService) {
    super(http, helperService);
  }

  public get schoolInfo() {
    const info = sessionStorage.getItem('schoolInfo')
    return info !== null ? JSON.parse(info) : null;
  }

  updateSchoolInfo(data, file) {
    const formData: FormData = new FormData();
    if (data) {
      const rqStr = JSON.stringify(data);
      formData.append('dto', new Blob([rqStr], {type: 'application/json'}))
      formData.append('logo', file);
    }
    return this.http.post(`${this.API}schools/update-by-code`, formData);
  }
}
