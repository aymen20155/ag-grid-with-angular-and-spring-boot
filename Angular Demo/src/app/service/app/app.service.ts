import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
// import 'rxjs/add/operator/map';
// import 'rxjs/add/operator/catch';
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
//import{map}  from 'rxjs/operators'
import { RequestWithFilterAndSort } from 'src/app/model/request-with-sort-filter';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private httpClient: HttpClient) { }

  getCompany(requestWithSortAndFilter: RequestWithFilterAndSort, page: number, size: number) :Observable<any> {
    let apiURL = 'http://localhost:8082/get-companies?page=' + page + "&size=" + size
    return this.httpClient.post(apiURL, requestWithSortAndFilter)
               .pipe(
                  tap(data => console.log("All data : " + JSON.stringify(data))),
                  catchError(this.handleError)
                );
  }

    getCompanies(from: number, to: number) :Observable<any> {
    let apiURL = 'http://localhost:8082/get-companies?start=' + from + "&end=" + to
    return this.httpClient.get(apiURL)
               .pipe(
                  tap(data => console.log("All data : " + JSON.stringify(data))),
                  catchError(this.handleError)
                );
  }

      private handleError(err: HttpErrorResponse) {
      // in a real world app, we may send the server to some remote logging infrastructure
      // instead of just logging it to the console
      let errorMessage = '';
      if (err.error instanceof ErrorEvent) {
        // A client-side or network error occurred. Handle it accordingly.
        errorMessage = `An error occurred: ${err.error.message}`;
      } else {
        // The backend returned an unsuccessful response code.
        // The response body may contain clues as to what went wrong,
        errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
      }
      console.error(errorMessage);
      return throwError(() => errorMessage);
    }
}
