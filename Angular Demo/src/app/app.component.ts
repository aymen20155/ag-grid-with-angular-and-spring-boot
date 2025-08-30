import { Component } from '@angular/core';
import { ColDef } from 'ag-grid-community';
import { ColumnApi, GridApi, GridOptions } from 'ag-grid-community/dist/lib/main';
import { OnInit } from '@angular/core';
import { AppService } from './service/app/app.service';
import { RequestWithFilterAndSort } from './model/request-with-sort-filter';
import { IDatasource, IGetRowsParams } from 'ag-grid-community';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  private gridApi!: GridApi;
  private gridColumnApi!: ColumnApi;
  defaultPageSize = 10


  constructor(private appService: AppService) { }
  ngOnInit(): void {
/*     let request: RequestWithFilterAndSort = { colId: undefined, sort: undefined, filterModel: undefined, data: undefined };
    this.appService.getCompany(request, 0, 10).subscribe((res: any) => {
      console.log("response data : ", res)
      //this.rowData=res['content']
    }, err => {
      console.log("errpr : ", err)
    })

    this.gridOptions.rowModelType = 'infinite'; */
  }
  title = 'Ag_Grid_Demo';

  columnDefs: ColDef[] = [
    { field: 'id' },
    { field: 'companyName' },
    { field: 'employeeName' },
    { field: 'description' },
    { field: 'leave' },
  ];

/*   rowData = [
    { id: 1245, companyName: 'Celica', employeeName: "raul", description: "description", leave: 2 },
    { id: 1245, companyName: 'Celica', employeeName: "raul", description: "description", leave: 2 },
    { id: 1245, companyName: 'Celica', employeeName: "raul", description: "description", leave: 2 },
    { id: 1245, companyName: 'Celica', employeeName: "raul", description: "description", leave: 2 },

  ]; */
    rowData = [];

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

      this.appService.getCompany(request, this.gridApi.paginationGetCurrentPage(), this.gridApi.paginationGetPageSize()).subscribe((response: any) => {
        params.successCallback(
          response["content"], response["totalElements"]
        );
      }, err => {
        console.log("error in data source : ", err)
      })
    }
  }


  onPageSizeChanged(event: any) {
    console.log("Inside onPageSizeChanged");
    this.gridApi.paginationSetPageSize(Number(event.target.value));
  }
}
