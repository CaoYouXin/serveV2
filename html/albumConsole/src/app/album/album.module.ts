import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EditorModule } from './editor/editor.module';

import { UploadService } from '../services/upload.service';
import { GalleryService } from '../services/gallery.service';
import { AlbumEditorService } from '../services/album-editor.service';
import { AlbumService } from '../services/album.service';
import { AlbumPhotoService } from '../services/album-photo.service';

import { AlbumComponent } from './album/album.component';
import { PhotoComponent } from './photo/photo.component';
import { OnePhotoComponent } from './one-photo/one-photo.component';
import { GalleryComponent } from './gallery/gallery.component';
import { OneAlbumComponent } from './one-album/one-album.component';
import { AlbumPhotoComponent } from './album-photo/album-photo.component';
import { OneAlbumPhotoComponent } from './one-album-photo/one-album-photo.component';

@NgModule({
  imports: [
    CommonModule,
    EditorModule
  ],
  declarations: [AlbumComponent, PhotoComponent, OnePhotoComponent, GalleryComponent, OneAlbumComponent, AlbumPhotoComponent, OneAlbumPhotoComponent],
  providers: [UploadService, GalleryService, AlbumEditorService, AlbumService, AlbumPhotoService]
})
export class AlbumModule { }
