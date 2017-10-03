import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { Subject } from 'rxjs/Subject';

@Injectable()
export class AlbumEditorService {

  albumEditorCmd: Subject<{ show: boolean }> = new Subject<{ show: boolean }>();

  constructor() { }

  bindAlbumEditorCmd() {
    return this.albumEditorCmd.asObservable();
  }

  newOne() {
    this.albumEditorCmd.next({ show: true });
  }

}
