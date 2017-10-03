import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { Subject } from 'rxjs/Subject';

@Injectable()
export class GalleryService {

  private galleryShow: Subject<{ show: boolean, idx: number }> = new Subject<{ show: boolean, idx: number }>();

  constructor() { }

  bindGalleryShow(): Observable<{ show: boolean, idx: number }> {
    return this.galleryShow.asObservable();
  }

  show(idx: number) {
    this.galleryShow.next({ show: true, idx });
  }

  hide() {
    this.galleryShow.next({ show: false, idx: -2 });
  }

}
