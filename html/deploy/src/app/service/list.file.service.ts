import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {API} from "../const/api.const";
import "rxjs/add/operator/map";

@Injectable()
export class ListFileService {

  constructor(private dao: DaoUtil) {}

  list(path) {
    return this.dao.get(API.getAPI("list/file")(path))
      .map(res => res.json())
      .map(ret => DaoUtil.checkCode(ret));
  }

}
