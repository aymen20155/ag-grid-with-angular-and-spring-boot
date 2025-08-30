import { Component } from '@angular/core';
import { ColDef } from 'ag-grid-community';
import { ColumnApi, GridApi, GridOptions } from 'ag-grid-community/dist/lib/main';
import { OnInit } from '@angular/core';
import { AppService } from './service/app/app.service';
import { RequestWithFilterAndSort } from './model/request-with-sort-filter';
import { IDatasource, IGetRowsParams } from 'ag-grid-community';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  private gridApi!: GridApi;
  private gridColumnApi!: ColumnApi;
  defaultPageSize = 10
  sub!: Subscription;
  errorMessage: string = '';
  title = 'Ag_Grid_Demo';
  rowData = [];

  constructor(private appService: AppService) { }
  ngOnInit(): void {

  }

  columnDefs: ColDef[] = [
    { field: 'id' },
    { field: 'companyName' },
    { field: 'employeeName' },
    { field: 'description' },
    { field: 'leave' },
  ];


  gridOptions: GridOptions = {
    defaultColDef: {
      sortable: true,
      filter: true,
    },
    rowModelType: 'infinite',
  }

  onGridReady(params: any) {
    console.log("Inside onGridReady");
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.gridApi.setDatasource(this.dataSource);
  }

  dataSource: IDatasource = {
    getRows: (params: IGetRowsParams) => {
      console.log("Inside getRows");
      let sort = undefined;
      let colId = undefined;
      if (params.sortModel[0]) {
        sort = params.sortModel[0].sort;
        colId = params.sortModel[0].colId;
      }
      let request: RequestWithFilterAndSort = { colId: colId, sort: sort, filterModel: params.filterModel, data: undefined };

      this.sub = this.appService
        .getCompany(request, this.gridApi.paginationGetCurrentPage(), this.gridApi.paginationGetPageSize())
        .subscribe({
          next: response => {
            params.successCallback(response["content"], response["totalElements"]);
          },
          error: err => {
            this.errorMessage = err;
            console.error(err);
          }
        });
    }
  }

  onPageSizeChanged(event: any) {
    console.log("Inside onPageSizeChanged");
    this.gridApi.paginationSetPageSize(Number(event.target.value));
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

}
