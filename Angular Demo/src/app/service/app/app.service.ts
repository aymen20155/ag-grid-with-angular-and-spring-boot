import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
// import 'rxjs/add/operator/map';
// import 'rxjs/add/operator/catch';
import { HttpClient } from "@angular/common/http";
//import{map}  from 'rxjs/operators'
import { RequestWithFilterAndSort } from 'src/app/model/request-with-sort-filter';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private httpClient: HttpClient,) { }

  getCompany(requestWithSortAndFilter: RequestWithFilterAndSort, page: number, size: number) {
    let apiURL = 'http://localhost:8082/get-companies?page=' + page + "&size=" + size
    return this.httpClient.post(apiURL, requestWithSortAndFilter);
  }
}
