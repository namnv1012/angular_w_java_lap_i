import {QueryParamsModel} from './query-models/query-params.model';
import {QueryResultsModel} from './query-models/query-results.model';

export class HttpExtenstionsModel {

  baseFilter(entities: any[], queryParams: QueryParamsModel, filtrationFields: string[] = []): QueryResultsModel {
    let entitiesResult = this.searchInArray(entities, queryParams.filter, filtrationFields);

    if (queryParams.sortField) {
      entitiesResult = this.sortArray(entitiesResult, queryParams.sortField, queryParams.sortOrder);
    }
    const totalCount = entitiesResult.length;
    const initialPos = queryParams.pageNumber * queryParams.pageSize;
    entitiesResult = entitiesResult.slice(initialPos, initialPos + queryParams.pageSize);

    const queryResults = new QueryResultsModel();
    queryResults.items = entitiesResult;
    queryResults.totalCount = totalCount;
    return queryResults;
  }

  sortArray(incomingArray: any[], sortField: string = '', sortOrder: string = 'asc'): any[] {
    if (!sortField) {
      return incomingArray;
    }

    let result: any[] = [];
    result = incomingArray.sort((a, b) => {
      if (a[sortField] < b[sortField]) {
        return sortOrder === 'asc' ? -1 : 1;
      }

      if (a[sortField] > b[sortField]) {
        return sortOrder === 'asc' ? 1 : -1;
      }

      return 0;
    });
    return result;
  }

  searchInArray(incomingArray: any[], queryObj: any, filtrationFields: string[] = []): any[] {
    const result: any[] = [];
    let resultBuffer: any[] = [];
    const indexes: number[] = [];
    let firstIndexes: number[] = [];
    let doSearch = false;

    filtrationFields.forEach(item => {
      if (item in queryObj) {
        incomingArray.forEach((element, index) => {
          if (element[item] === queryObj[item]) {
            firstIndexes.push(index);
          }
        });
        firstIndexes.forEach(element => {
          resultBuffer.push(incomingArray[element]);
        });
        incomingArray = resultBuffer.slice(0);
        resultBuffer = [].slice(0);
        firstIndexes = [].slice(0);
      }
    });

    Object.keys(queryObj).forEach(key => {
      const searchText = queryObj[key].toString().trim().toLowerCase();
      if (key && !filtrationFields.some(e => e === key) && searchText) {
        doSearch = true;
        try {
          incomingArray.forEach((element, index) => {
            if (element[key]) {
              const val = element[key].toString().trim().toLowerCase();
              if (val.indexOf(searchText) > -1 && indexes.indexOf(index) === -1) {
                indexes.push(index);
              }
            }
          });
        } catch (ex) {
          console.log(ex, key, searchText);
        }
      }
    });

    if (!doSearch) {
      return incomingArray;
    }

    indexes.forEach(re => {
      result.push(incomingArray[re]);
    });

    return result;
  }
}
