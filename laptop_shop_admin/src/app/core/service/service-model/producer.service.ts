import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {AbstractControl, AsyncValidatorFn, ValidationErrors, FormControl} from '@angular/forms';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProducerService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}producers`)
  }

  addProducer(producer: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}producers`, producer);
  }

  searchProducer(searchProducer: any, page: number, pageSize: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}producers/search?page=${page}&pageSize=${pageSize}`, searchProducer);
  }
  deleteProducerById(id: number):Observable<any>{
    return this.httpClient.post<any>(`${this.API}producers/delete`, id);
  }
  getByCode(producerCode: string) {
    return this.httpClient.get(`${this.API}producers/findByCode?code=${producerCode}`)
  }

  findByCodeAndId(producerCode: string, id: number) {
    return this.httpClient.get(`${this.API}producers/findByCode`,
      {params: new HttpParams().set('code', producerCode).set('id', String(id))});
  }

  getAllByStatusActive():Observable<any>{
    return this.httpClient.get<any>(`${this.API}producers/getAllByStatusActive`);
  }

  validateCode(id: number): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.findByCodeAndId(control.value, id).pipe(map((res : any) => {
        if (res !== undefined) {
          return (res !== null && res?.id !== id) ? {codeExitsts: true} : null;
        }
      }))
    };
  }
}
