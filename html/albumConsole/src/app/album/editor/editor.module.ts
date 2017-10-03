import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { EditAlbumComponent } from './edit-album/edit-album.component';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  declarations: [EditAlbumComponent],
  exports: [EditAlbumComponent]
})
export class EditorModule { }
