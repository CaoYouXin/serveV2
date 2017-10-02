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

  constructor(private photoService: PhotoService) { }

  ngOnInit() {
    this.photoService.list(1, 10, (photos => this.photos = photos.map(photo => ({ ...photo, src: API.getAPI("domain") + photo.AlbumPhotoUrl }))))
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
