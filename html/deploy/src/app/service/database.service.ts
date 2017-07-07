import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import "rxjs/add/operator/map";
import {API} from "../const/api.const";

@Injectable()
export class DatabaseService {

  constructor(private dao: DaoUtil) {
  }

  status() {
    return this.dao.get(API.getAPI("database/status"))
      .map(res => res.json())
      .map(ret => DaoUtil.checkCode(ret));
  }

}
