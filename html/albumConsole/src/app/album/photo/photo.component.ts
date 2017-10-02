import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { PhotoService } from '../../services/photo.service';
import { API } from '../../services/api.const';

@Component({
  selector: 'app-photo',
  templateUrl: './photo.component.html',
  styleUrls: ['./photo.component.css']
})
export class PhotoComponent implements OnInit {

  @ViewChild("f")
  f: ElementRef;

  @ViewChild("file")
  file: ElementRef;

  @ViewChild("wrapper")
  wrapper: ElementRef;

  photos: Array<any> = [];
  page: number;
  total: number;
  size: number;
  canAdd: boolean;
  uploading: number;

  constructor(private photoService: PhotoService) { }

  ngOnInit() {
    this.size = this.total = 2 * Math.floor(this.wrapper.nativeElement.offsetWidth / 222);
    this.page = 1;
    this.goToPage(this.page);
  }

  goToPage(page) {
    if (page < 1 || page > this.getLastPage()) {
      return;
    }

    const self = this;
    self.photoService.list(page, self.size, (paged) => {
      self.page = page;
      self.total = paged.Total || self.size;
      self.photos = paged.Photos.map(photo => ({
        ...photo,
        online: true,
        src: API.getAPI("domain") + photo.AlbumPhotoUrl
      }));

      self.canAdd = page === this.getLastPage();
    });
  }

  goToLastPage() {
    this.goToPage(this.getLastPage());
  }

  getLastPage() {
    return Math.floor(this.total / this.size) + (this.total % this.size === 0 ? 0 : 1);
  }

  selectFile() {
    if (this.uploading) {
      return;
    }

    this.file.nativeElement.value = null;
    this.file.nativeElement.click();
  }

  selectedFile() {
    const self = this;
    const files = this.file.nativeElement.files;
    let filtered = false;
    for (let i = 0; i < files.length; i++) {
      let file = files[i];
      if (file.type.indexOf('image') === -1) {
        filtered = true;
        continue;
      }

      self.photos = [...self.photos, Object.assign(file, { online: false })];
    }
    this.total += files.length;
    this.uploading = files.length;

    if (filtered) {
      alert("已过滤非图片文件");
    }
  }

  onComplete() {
    this.uploading--;

    if (!this.uploading) {
      this.goToLastPage();
    }
  }

}
