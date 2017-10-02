import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { UserService } from './user.service';
import { API } from './api.const';

@Injectable()
export class PhotoService {

  constructor(private http: HttpService, private user: UserService) { }

  add(tmpPath, serveDir, md5, fileName, suc) {
    const self = this;
    self.http.postJSON(API.getAPI("copy/file"), {
      Src: [tmpPath + md5 + '/' + fileName],
      Dst: serveDir + this.user.getUserId() + '/' + md5 + '/'
    }).subscribe(
      ret => {
        self.http.postJSON(API.getAPI("photo/save"), {
          'UserId': this.user.getUserId(),
          'AlbumPhotoUrl': serveDir + this.user.getUserId() + '/' + md5 + '/' + fileName,
        }).subscribe(ret => suc(ret), err => console.log('保存照片信息失败'));
      },
      err => console.log('服务器上复制文件出错！')
      );
  }

  list(page, size, suc) {
    const self = this;
    self.http.getJSON(API.getAPI("photo/list")(page, size)).subscribe(ret => suc(ret));
  }

}
