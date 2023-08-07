import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {NotiService} from './notification.service';
import {environment} from '../../../../environments/environment';
import {BasicService} from '../utils/basic.service';
import {HelperService} from '../utils/helper.service';
import {CommonServiceService} from '../utils/common-service.service';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})

export class DocumentaryService extends BasicService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private http: HttpClient, private notiService: NotiService, private commonService: CommonServiceService,
              public helperService: HelperService) {
    super(http, helperService);
  }

  export(data) {
    const url = this.API + `documentaries/exportExcel`;
    return this.commonService.downloadFile(url, data, null, `DS_congvan_vanban_${moment().format('DDMMYYYY').toString()}.xlsx`);
  }

  create(formData: FormData, data) {
    if (data) {
      formData.append('code', data.code.trim());
      formData.append('documentType', data.documentType);
      formData.append('releaseDate', new Date(data.releaseDate).toISOString());
      formData.append('effectiveDate', new Date(data.effectiveDate).toISOString());
      formData.append('signer', data.signer);
      formData.append('compendia', data.compendia.trim());
    }
    return this.http.post(`${this.API}documentaries/create`, formData);
  }

  update(formData: FormData, data) {
    if (data) {
      formData.append('id', data.id);
      formData.append('code', data.code.trim());
      formData.append('documentType', data.documentType);
      formData.append('releaseDate', new Date(data.releaseDate).toISOString());
      formData.append('effectiveDate', new Date(data.effectiveDate).toISOString());
      formData.append('signer', data.signer);
      formData.append('compendia', data.compendia.trim());
      formData.append('file', data.file);
    }
    return this.http.post(`${this.API}documentaries/update`, formData);
  }

  getData(data) {
    return this.httpClient.get<any>(`${this.API}documentaries/${data.id}`, null);
  }
}
