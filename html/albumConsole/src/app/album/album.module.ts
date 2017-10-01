import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlbumComponent } from './album/album.component';
import { PhotoComponent } from './photo/photo.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [AlbumComponent, PhotoComponent]
})
export class AlbumModule { }
