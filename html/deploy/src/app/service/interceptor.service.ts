import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {API} from "../const/api.const";
import "rxjs/add/operator/map";

@Injectable()
export class InterceptorService {

  constructor(private dao: DaoUtil) {
  }

  list() {
    return this.dao.getJSON(API.getAPI("inter/list"));
  }

  set(className) {
    return this.dao.getJSON(API.getAPI("inter/set")(className));
  }

  setDisabled(id, disabled) {
    return this.dao.getJSON(API.getAPI("inter/set/dis")(id, disabled));
  }

}
