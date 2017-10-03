import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { Subject } from 'rxjs/Subject';
import { HttpService } from './http.service';
import { API } from './api.const';

@Injectable()
export class AlbumEditorService {

  private albumEditorCmd: Subject<{ show: boolean, model: any }> = new Subject<{ show: boolean, model: any }>();

  constructor(private http: HttpService) { }

  bindAlbumEditorCmd() {
    return this.albumEditorCmd.asObservable();
  }

  newOne() {
    this.albumEditorCmd.next({ show: true, model: {} });
  }

  cancel() {
    this.albumEditorCmd.next({ show: false, model: null });
  }

  save(model, suc, fail) {
    const self = this;
    self.http.postJSON(API.getAPI("album/save"), model)
      .subscribe((ret) => {
        suc(ret);
        self.cancel();
      }, fail);
  }

  edit(model) {
    this.albumEditorCmd.next({ show: true, model });
  }

}
