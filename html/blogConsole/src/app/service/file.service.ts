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

  delete(path) {
    return this.dao.getJSON(API.getAPI("delete/file")(path));
  }

  create(path) {
    return this.dao.getJSON(API.getAPI("create/file")(path));
  }

  copy(src, dst) {
    return this.dao.postJSON(API.getAPI("copy/file"), {
      Src: src,
      Dst: dst
    });
  }

}
