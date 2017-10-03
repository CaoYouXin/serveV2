import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EditorModule } from './editor/editor.module';

import { UploadService } from '../services/upload.service';
import { PhotoService } from '../services/photo.service';
import { GalleryService } from '../services/gallery.service';
import { AlbumEditorService } from '../services/album-editor.service';

import { AlbumComponent } from './album/album.component';
import { PhotoComponent } from './photo/photo.component';
import { OnePhotoComponent } from './one-photo/one-photo.component';
import { GalleryComponent } from './gallery/gallery.component';
import { OneAlbumComponent } from './one-album/one-album.component';

@NgModule({
  imports: [
    CommonModule,
    EditorModule
  ],
  declarations: [AlbumComponent, PhotoComponent, OnePhotoComponent, GalleryComponent, OneAlbumComponent],
  providers: [UploadService, PhotoService, GalleryService, AlbumEditorService]
})
export class AlbumModule { }
