import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {Subject} from "rxjs/Subject";

@Injectable()
export class SoundService {

  static ROUTE_CLICK = "ROUTE_CLICK";

  private cmdSubject = new Subject<any>();

  getCmd(): Observable<any> {
    return this.cmdSubject.asObservable();
  }

  addCmd(cmd) {
    this.cmdSubject.next(cmd);
  }

}
