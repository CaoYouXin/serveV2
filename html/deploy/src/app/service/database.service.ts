import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import "rxjs/add/operator/map";
import {API} from "../const/api.const";

@Injectable()
export class DatabaseService {

  constructor(private dao: DaoUtil) {
  }

  status() {
    return this.dao.getJSON(API.getAPI("database/status"));
  }

  init(schema: string, header: any = {}) {
    return this.dao.getJSON(API.getAPI("database/init")(schema), header);
  }

}
