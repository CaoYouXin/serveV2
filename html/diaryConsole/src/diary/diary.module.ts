import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from "@angular/forms";

import { HomeComponent } from './home';
import { BookComponent } from './book';
import { PageComponent } from './page';
import { TableComponent } from './table';
import { NewPageComponent } from './newpage';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  declarations: [
    HomeComponent,
    BookComponent,
    PageComponent,
    TableComponent,
    NewPageComponent
  ]
})
export class DiaryModule { }
