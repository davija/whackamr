import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MergeRequestDto } from '@models/app.model';
import { AgGridAngular } from 'ag-grid-angular';
import { ColDef } from 'ag-grid-community';

@Component({
  standalone: true,
  imports: [MatTableModule, MatButtonModule, AgGridAngular],
  templateUrl: './merge-requests-page.component.html',
})
export class MergeRequestsPageComponent {
  defaultColumnDef: ColDef = {
    filter: 'agTextColumnFilter',
    floatingFilter: true,
  };
  columnDefs: ColDef[] = [
    {
      field: 'username',
    },
    {
      field: 'link',
    },
  ];

  protected displayedColumns = ['request-owner', 'request-link', 'request-actions'];
  protected mergeRequests: MergeRequestDto[] = [
    {
      mergeRequestId: 1,
      link: 'link 1',
      owner: {
        username: 'davija',
        userid: 1,
        firstName: 'James',
        lastName: 'Davis',
        emailAddress: 'test@example.com',
        active: true,
      },
      active: true,
      relatedRequests: [],
    },
  ];
}
