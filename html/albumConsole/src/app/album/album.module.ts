import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlbumComponent } from './album/album.component';
import { PhotoComponent } from './photo/photo.component';
import { OnePhotoComponent } from './one-photo/one-photo.component';

import { UploadService } from '../services/upload.service';
import { PhotoService } from '../services/photo.service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [AlbumComponent, PhotoComponent, OnePhotoComponent],
  providers: [UploadService, PhotoService]
})
export class AlbumModule { }
