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
import { PhotoComponent } from './photo';

import { TableletService, FileService, UploadService } from '../service';

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
    MilestoneComponent,
    PhotoComponent
  ],
  providers: [TableletService, FileService, UploadService]
})
export class DiaryModule { }
