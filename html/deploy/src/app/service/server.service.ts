import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {API} from "../const/api.const";
import "rxjs/add/operator/map";

@Injectable()
export class ServerService {

  constructor(private dao: DaoUtil) {
  }

  restart() {
    return this.dao.getJSON(API.getAPI("server/restart"));
  }

}
