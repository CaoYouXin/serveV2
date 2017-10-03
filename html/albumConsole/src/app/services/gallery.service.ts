import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { Subject } from 'rxjs/Subject';

@Injectable()
export class GalleryService {

  private galleryShow: Subject<{ show: boolean, idx: number, data: Array<any> }> = new Subject<{ show: boolean, idx: number, data: Array<any> }>();

  constructor() { }

  bindGalleryShow(): Observable<{ show: boolean, idx: number, data: Array<any> }> {
    return this.galleryShow.asObservable();
  }

  show(idx: number, data: Array<any>) {
    this.galleryShow.next({ show: true, idx, data });
  }

  hide() {
    this.galleryShow.next({ show: false, idx: -2, data: [] });
  }

}
