import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {DaoUtil} from "../http/index";
import {API} from "../const/index";

@Injectable()
export class BlogService {

  constructor(private dao: DaoUtil) {}

  init(): Observable<any> {
    return this.dao.getJSON(API.getAPI("blog/init"));
  }

}