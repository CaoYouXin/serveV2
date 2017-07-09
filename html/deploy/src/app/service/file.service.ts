import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {API} from "../const/api.const";
import "rxjs/add/operator/map";

@Injectable()
export class FileService {

  constructor(private dao: DaoUtil) {}

  list(path) {
    return this.dao.get(API.getAPI("list/file")(path))
      .map(res => res.json())
      .map(ret => DaoUtil.checkCode(ret));
  }

  unzip(path, to) {
    return this.dao.get(API.getAPI("unzip/file")(path, to))
      .map(res => res.json())
      .map(ret => DaoUtil.checkCode(ret));
  }

}
