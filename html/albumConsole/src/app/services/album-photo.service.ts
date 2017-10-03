import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { API } from './api.const';

@Injectable()
export class AlbumPhotoService {

  constructor(private http: HttpService) { }

  attach(albumId, photoId, suc) {
    this.http.getJSON(API.getAPI("attach/album/photo")(albumId, photoId)).subscribe(suc);
  }

  release(albumId, photoId, suc) {
    this.http.getJSON(API.getAPI("release/album/photo")(albumId, photoId)).subscribe(suc);
  }

}
