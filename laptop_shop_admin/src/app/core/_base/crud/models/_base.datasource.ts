import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {Observable, BehaviorSubject, Subscription} from 'rxjs';
import {BaseModel} from './_base.model';
import {skip, distinctUntilChanged} from 'rxjs/operators';

export class BaseDataSource implements DataSource<BaseModel> {
  entitySubject = new BehaviorSubject<any[]>([]);
  hasItems = true;

  paginatorTotalSubject = new BehaviorSubject<number>(0);
  paginatorTotal$: Observable<number>;
  subscriptions: Subscription[] = [];

  constructor() {
    this.paginatorTotal$ = this.paginatorTotalSubject.asObservable();

    const hasItemsSubscription = this.paginatorTotal$.pipe(
      distinctUntilChanged(),
      skip(1)
    ).subscribe(res => this.hasItems = res > 0);
    this.subscriptions.push(hasItemsSubscription);
  }

  connect(collectionViewer: CollectionViewer): Observable<any[]> {
    return this.entitySubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.entitySubject.complete();
    this.paginatorTotalSubject.complete();
    this.subscriptions.forEach(sb => sb.unsubscribe());
  }
}
