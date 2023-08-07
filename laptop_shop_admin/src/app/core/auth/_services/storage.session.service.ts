import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({providedIn: 'root'})
export class StorageSessionService {
  public readonly SCHOOL_INFO = 'schoolInfo';
  private storage: Storage;
  private subjects: Map<string, BehaviorSubject<any>>;

  constructor() {
    this.storage = sessionStorage;
    this.subjects = new Map<string, BehaviorSubject<any>>();
  }

  get(key: string): any {
    let item = this.storage.getItem(key);
    if (item === 'undefined') {
      item = undefined;
    } else {
      item = JSON.parse(item);
    }
    return item;
  }

  set(key: string, value: any) {
    this.storage.setItem(key, JSON.stringify(value));
    if (!this.subjects.has(key)) {
      this.subjects.set(key, new BehaviorSubject<any>(value));
    } else {
      this.subjects.get(key).next(value);
    }
  }

  remove(key: string) {
    if (this.subjects.has(key)) {
      this.subjects.get(key).complete();
      this.subjects.delete(key);
    }
    this.storage.removeItem(key);
  }

  clear() {
    this.subjects.clear();
    this.storage.clear();
  }
}
