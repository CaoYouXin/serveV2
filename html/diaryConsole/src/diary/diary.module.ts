import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from "@angular/forms";

import { HomeComponent } from './home';
import { BookComponent } from './book';
import { PageComponent } from './page';
import { TableComponent } from './table';
import { NewPageComponent } from './newpage';
import { EditBookComponent } from './editbook';
import { EidtBookPageComponent } from './editbookpage';
import { MilestoneComponent } from './milestone';

import { TableletService } from '../service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [
    HomeComponent,
    BookComponent,
    PageComponent,
    TableComponent,
    NewPageComponent,
    EditBookComponent,
    EidtBookPageComponent,
    MilestoneComponent
  ],
  providers: [TableletService]
})
export class DiaryModule { }
