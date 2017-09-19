import { Component, OnInit, ViewChild, ElementRef } from "@angular/core";
import { Router } from "@angular/router";
import { TableletService, UploadService, FileService } from "../../service";
import { API, DaoUtil, RestCode } from "../../http";
import { Md5 } from "ts-md5/dist/md5";

@Component({
  selector: 'diary-photo',
  templateUrl: './photo.component.html',
  styleUrls: ['./photo.component.css']
})
export class PhotoComponent implements OnInit {

  server: string = API.getAPI("server/origin") + '/';

  photos: Array<any> = [];

  @ViewChild("f")
  f: ElementRef;

  @ViewChild("file")
  file: ElementRef;

  constructor(private router: Router,
    private tablelet: TableletService,
    private dao: DaoUtil,
    private rest: RestCode,
    private uploader: UploadService,
    private fileService: FileService) { }

  ngOnInit() {
    const self = this;
    this.dao.getJSON(API.getAPI("photo/list")(
      this.tablelet.getHandlingData(TableletService.PAGEs).DiaryPageId
    )).subscribe(
      ret => self.rest.checkCode(ret, retBody => {
        self.photos = retBody;
      }),
      err => DaoUtil.logError(err)
      );
  }

  selectFile() {
    this.file.nativeElement.value = null;
    this.file.nativeElement.click();
  }

  selectedFile() {
    const self = this;
    const md5 = Md5.hashStr(new Date() + '');
    this.uploader.send(this.f.nativeElement,
      API.getAPI("upload/code")("/tmp/" + md5 + '/'),
      (responseText, filename) => {
        let ret = JSON.parse(responseText);
        if (ret.code !== 20000) {
          return;
        }

        const serveDir = 'serve/screenshot/' + md5 + '/';
        self.fileService.copy(['upload/tmp/' + md5 + '/' + filename], serveDir).subscribe(
          ret => self.rest.checkCode(ret, (retBody) => {

            self.dao.postJSON(API.getAPI("photo/save"), {
              DiaryPageId: self.tablelet.getHandlingData(TableletService.PAGEs).DiaryPageId,
              DiaryPhotoUrl: serveDir + filename,
              DiaryPhotoDisabled: false
            }).subscribe(
              ret => self.rest.checkCode(ret, (retBody) => {
                self.photos.push(retBody);
              }),
              err => DaoUtil.logError(err)
              );
          }),
          err => DaoUtil.logError(err)
        );
      });
  }

  change(photo, idx) {
    const self = this;
    self.dao.postJSON(API.getAPI("photo/save"), {
      ...photo,
      DiaryPhotoDisabled: !photo.DiaryPhotoDisabled
    }).subscribe(
      ret => self.rest.checkCode(ret, retBody => {
        self.photos[idx] = retBody;
      }),
      err => DaoUtil.logError(err)
      );
  }

}