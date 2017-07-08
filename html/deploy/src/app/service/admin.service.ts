import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import "rxjs/add/operator/map";
import {API} from "../const/api.const";
import {Md5} from "ts-md5/dist/md5";

@Injectable()
export class AdminService {

  private returnURL: string;

  constructor(private dao: DaoUtil) {
  }

  setReturnUrl(url) {
    this.returnURL = url;
  }

  getReturnUrl() {
    return this.returnURL;
  }

  setting(model) {
    return this.dao.post(API.getAPI("admin/setting"), {
      OldUserName: model.oldUsername,
      UserName: model.username,
      OldPassword: model.oldPassword ? Md5.hashStr(model.oldPassword) : model.oldPassword,
      Password: Md5.hashStr(model.password)
    })
      .map(res => res.json())
      .map(ret => DaoUtil.checkCode(ret));
  }

  check() {
    return this.dao.get(API.getAPI("admin/check"))
      .map(res => res.json())
      .map(ret => DaoUtil.checkCode(ret));
  }

  verify(model) {
    return this.dao.post(API.getAPI("admin/verify"), {
      UserName: model.username,
      Password: Md5.hashStr(model.password)
    })
      .map(res => res.json())
      .map(ret => DaoUtil.checkCode(ret));
  }

}
