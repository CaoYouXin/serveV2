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

  photos: Array<any> = [];
  page: number = 1;
  total: number = 10;
  size: number = 10;

  constructor(private photoService: PhotoService) { }

  ngOnInit() {
    this.goToPage(this.page);
  }

  goToPage(page) {
    if (page < 1 || page > this.getLastPage()) {
      return;
    }

    const self = this;
    self.photoService.list(page, self.size, (paged) => {
      self.page = page;
      self.total = paged.Total;
      self.photos = paged.Photos.map(photo => ({
        ...photo,
        online: true,
        src: API.getAPI("domain") + photo.AlbumPhotoUrl
      }));
    });
  }

  goToLastPage() {
    this.goToPage(this.getLastPage());
  }

  getLastPage() {
    return Math.floor(this.total / this.size) + (this.total % this.size === 0 ? 0 : 1);
  }

  selectFile() {
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

    if (filtered) {
      alert("已过滤非图片文件");
    }
  }

}
