import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { API } from './api.const';

@Injectable()
export class AlbumService {

  constructor(private http: HttpService) { }

  list(suc) {
    this.http.getJSON(API.getAPI("album/list")).subscribe((ret) => suc(ret));
  }

  delete(albumId, suc) {
    this.http.postJSON(API.getAPI("album/save"), {
      'AlbumId': albumId,
      'AlbumDisabled': true
    }).subscribe(suc);
  }

}
