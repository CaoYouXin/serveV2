import {Injectable} from "@angular/core";
import {DaoUtil} from "../http";
import {API} from "../const/api.const";
import "rxjs/add/operator/map";

@Injectable()
export class FileService {

  constructor(private dao: DaoUtil) {
  }

  list(path) {
    return this.dao.getJSON(API.getAPI("list/file")(path));
  }

  unzip(path, to) {
    return this.dao.getJSON(API.getAPI("unzip/file")(path, to));
  }

}
