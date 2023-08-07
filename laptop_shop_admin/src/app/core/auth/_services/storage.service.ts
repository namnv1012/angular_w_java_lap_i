import {Injectable} from '@angular/core';

@Injectable({providedIn: 'root'})
export class StorageService {
  private storage: any;

  constructor() {
    this.storage = localStorage; // localStorage || sessionStorage;
  }

  public store(key: string, value: any) {
    this.storage.setItem(key, JSON.stringify(value));
  }

  public remove(key: string) {
    this.storage.removeItem(key);
  }
}
