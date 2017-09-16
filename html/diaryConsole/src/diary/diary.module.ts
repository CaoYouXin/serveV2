import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeComponent } from './home';
import { BookComponent } from './book';
import { PageComponent } from './page';
import { TableComponent } from './table';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    HomeComponent,
    BookComponent,
    PageComponent,
    TableComponent
  ]
})
export class DiaryModule { }
