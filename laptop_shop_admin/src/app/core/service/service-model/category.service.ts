import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {CategoryModel} from '../model/category.model';
import {AbstractControl, AsyncValidatorFn, ValidationErrors} from '@angular/forms';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private API = `${environment.API_GATEWAY_ENDPOINT}`;

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.httpClient.get(`${this.API}categories`)
  }

  addCategory(category: CategoryModel): Observable<any> {
    return this.httpClient.post<any>(`${this.API}categories`, category);
  }

  searchCategory(searchCategory: any, page: number, pageSize: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}categories/search?page=${page}&pageSize=${pageSize}`, searchCategory);
  }

  deleteCategory(id: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API}categories/delete`, id);
  }

  getByCode(categoryCode: string) {
    return this.httpClient.get(`${this.API}categories/findByCode?code=${categoryCode}`)
  }

  findByCodeAndId(categoryCode: string, id: number) {
    return this.httpClient.get(`${this.API}categories/findByCode`,
      {params: new HttpParams().set('code', categoryCode).set('id', String(id))});
  }

  getAllByStatusActive():Observable<any>{
    return this.httpClient.get<any>(`${this.API}categories/getAllByStatusActive`);
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
