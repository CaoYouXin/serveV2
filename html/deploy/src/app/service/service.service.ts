import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {API} from "../const/api.const";
import "rxjs/add/operator/map";

@Injectable()
export class ServiceService {

  constructor(private dao: DaoUtil) {
  }

  list() {
    return this.dao.getJSON(API.getAPI("service/list"));
  }

  set(intf: string, impl: string) {
    return this.dao.getJSON(API.getAPI("service/set")(intf, impl));
  }

  disable(id: number, disabled: boolean) {
    return this.dao.getJSON(API.getAPI("service/set/disable")(id, disabled ? "T" : "F"));
  }

}
