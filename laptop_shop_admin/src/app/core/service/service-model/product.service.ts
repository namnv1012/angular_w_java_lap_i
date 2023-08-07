import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {AbstractControl, AsyncValidatorFn, ValidationErrors} from '@angular/forms';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}products`)
  }

  searchProduct(searchProducer: any, page: number, pageSize: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}products/search?page=${page}&pageSize=${pageSize}`, searchProducer);
  }

  saveProduct(producer: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}products/saveProduct`, producer);
  }

  findById(id: any): Observable<any> {
    return this.httpClient.get(`${this.API}products/findById/${id}`);
  }

  importQuantity(product): Observable<any> {
    return this.httpClient.post<any>(`${this.API}products/import-quantity`, product);
  }

  getByCode(producerCode: string) {
    return this.httpClient.get(`${this.API}products/findByCode?code=${producerCode}`)
  }

  validateCode(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.getByCode(control.value).pipe(map(res => {
        if (res !== undefined) {
          return res !== null ? {codeExitsts: true} : null;
        }
      }))
    };
  }
}
