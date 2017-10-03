import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlbumComponent } from './album/album.component';
import { PhotoComponent } from './photo/photo.component';
import { OnePhotoComponent } from './one-photo/one-photo.component';

import { UploadService } from '../services/upload.service';
import { PhotoService } from '../services/photo.service';
import { GalleryService } from '../services/gallery.service';
import { GalleryComponent } from './gallery/gallery.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [AlbumComponent, PhotoComponent, OnePhotoComponent, GalleryComponent],
  providers: [UploadService, PhotoService, GalleryService]
})
export class AlbumModule { }
