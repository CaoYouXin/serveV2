import { Component, OnInit, HostBinding, Input, Output, EventEmitter } from '@angular/core';
import { UploadService } from '../../services/upload.service';
import { PhotoService } from '../../services/photo.service';
import { API } from '../../services/api.const';
import { Md5 } from 'ts-md5/dist/md5';

@Component({
  selector: 'app-one-photo',
  templateUrl: './one-photo.component.html',
  styleUrls: ['./one-photo.component.css']
})
export class OnePhotoComponent implements OnInit {

  @HostBinding("style.width") hostWidth: string = "202px";
  @HostBinding("style.height") hostHeight: string = "202px";
  @HostBinding("style.border") hostBorder: string = "1px solid #eee";
  @HostBinding("style.margin") hostMargin: string = "20px 0 0 20px";

  @Input('data')
  data: any;

  @Output('complete')
  complete: EventEmitter<any> = new EventEmitter<any>();

  show: boolean;

  constructor(private uploader: UploadService, private photoService: PhotoService) { }

  ngOnInit() {
    if (!this.data.online) {
      let fr1 = new FileReader();
      fr1.onloadend = this.onReadURL.bind(this);
      fr1.readAsDataURL(this.data);
    }
  }

  onReadBinary(e) {
    const self = this;
    const md5 = Md5.hashStr(new Date() + '');
    self.uploader.uploadSoloFile(self.data.name, e.target.result,
      "/tmp/" + md5 + '/', (percentage) => self.data.percentage = (100 * (1 - percentage)).toFixed(0) + '%',
      () => self.photoService.add(
        "upload/tmp/",
        'serve/photos/',
        md5,
        self.data.name,
        (ret) => {
          self.data = {
            ...ret,
            ...self.data,
            online: true,
            src: API.getAPI("domain") + ret.AlbumPhotoUrl
          };
          self.complete.emit();
        }
      ));
  }

  onReadURL(e) {
    this.data.src = e.target.result;

    let fr2 = new FileReader();
    fr2.onload = this.onReadBinary.bind(this);
    fr2.readAsBinaryString(this.data);
  }

  showInfo() {
    this.show = true;
  }

  hideInfo() {
    this.show = false;
  }

}
