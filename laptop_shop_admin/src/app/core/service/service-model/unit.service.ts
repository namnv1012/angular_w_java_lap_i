import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {NotiService} from './notification.service';
import {environment} from '../../../../environments/environment';
import {GridParam} from '../model/grid-param';
import {BasicService} from '../utils/basic.service';
import {HelperService} from '../utils/helper.service';
import {Department} from '../model/department.model';
import {CommonServiceService} from '../utils/common-service.service';
import * as moment from 'moment';


@Injectable({
  providedIn: 'root'
})

export class DepartmentService extends BasicService {
  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private http: HttpClient, private notiService: NotiService, private commonService: CommonServiceService,
              public helperService: HelperService) {
    super(http, helperService);
  }


  addDepartment(department: Department) {
    return this.http.post<any>(`${this.API}departments/create`, department);
  }

  updateDepartment(department: Department) {
    return this.http.post<any>(`${this.API}departments/update`, department);
  }

  apiGetAll() {
    return this.http.get(`${this.API}departments/getAll`).toPromise();
  }

  apiGetDataTree(code, name, type, typeSearch) {
    const param = {
      code,
      name,
      type,
      typeSearch
    }
    return this.http.post(`${this.API}departments/search`, param).toPromise();
  }

  getTypeUnit(depId: number) {
    let params = '?';
    params += !!depId ? `depId=${depId}&` : '';
    return this.http.get(`${this.API}departments/getTypeUnit${params}`).toPromise();
  }

  getTeacherByDept(depId: number) {
    let params = '?';
    params += !!depId ? `depId=${depId}&` : '';
    return this.http.get(`${this.API}departments/getTeacherByDept${params}`).toPromise();
  }
  deleteSchool(id) {
    return this.http.post(`${this.API}departments/delete?id=${id}`, {});
  }

  exportTemplate() {
    const url = this.API + `departments/exportTemplate`;
    return this.commonService.downloadFile(url, null, null, `DS_donvithuoctruong_${moment().format('YYYYMMDD').toString()}`);
  }

  exportData(listData) {
    const url = this.API + `departments/exportData`;
    return this.commonService.downloadFile(url, listData, null, `DS_donvithuoctruong_${moment().format('YYYYMMDD').toString()}`);
  }

  upload(formData: FormData, typeImport) {
    return this.http.post(`${this.API}departments/import?typeImport=${typeImport}`, formData);
  }

  exportDataErrors(listErr: []) {
    const url = this.API + `departments/exportDataErrors`;
    return this.commonService.downloadFile(url, listErr, null, `DS_Import_Loi`);
  }
}
