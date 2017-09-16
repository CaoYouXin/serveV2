import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeComponent } from './home';
import { BookComponent } from './book';
import { PageComponent } from './page';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    HomeComponent,
    BookComponent,
    PageComponent
  ]
})
export class DiaryModule { }
