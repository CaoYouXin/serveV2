import {Injectable} from "@angular/core";
import {DaoUtil} from "caols-common-modules";
import {API} from "../const/api.const";
import "rxjs/add/operator/map";

@Injectable()
export class ServeAuthService {

  constructor(private dao: DaoUtil){
  }

  set(className: string) {
    return this.dao.getJSON(API.getAPI("serve/auth/set")(className));
  }

}
