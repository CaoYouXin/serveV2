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

  verify(model) {
    return this.dao.postJSON(API.getAPI("admin/verify"), {
      UserName: model.username,
      Password: Md5.hashStr(model.password)
    });
  }

}
