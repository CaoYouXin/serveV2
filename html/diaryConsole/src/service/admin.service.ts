import { Injectable } from "@angular/core";
import { DaoUtil } from "../http/index";
import { Observable } from "rxjs";
import "rxjs/add/operator/map";
import { API, RestCode } from "../http";
import { Md5 } from "ts-md5/dist/md5";

@Injectable()
export class AdminService {

  constructor(private dao: DaoUtil, private rest: RestCode) {
  }

  verify(model, sucFn, rcvFn): void {
    const self = this;
    self.dao.postJSON(API.getAPI("admin/verify"), {
      UserName: model.username,
      Password: Md5.hashStr(model.password)
    }).subscribe(
      ret => {
        self.rest.checkCode(ret, retBody => {
          if (typeof sucFn === "function") sucFn(retBody);
        });
        if (typeof rcvFn === "function") rcvFn();
      },
      error => {
        if (typeof rcvFn === "function") rcvFn();
        DaoUtil.logError(error);
      }
      );
  }

}
