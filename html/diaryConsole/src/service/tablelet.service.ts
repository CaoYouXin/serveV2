import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Subject } from "rxjs/Subject";
import { ServiceUtils } from "./service.util";
import { DaoUtil, RestCode } from "../http";

@Injectable()
export class TableletService {

  static PAGEs = "PAGEs";
  static BOOKs = "BOOKs";
  // static MILESTONEs = "MILESTONEs";

  private dataSubjects = {};
  private lastData = JSON.parse(localStorage.getItem('lastData') || "{}");
  private handlingData = JSON.parse(localStorage.getItem('handlingData') || "{}");

  constructor(private dao: DaoUtil, private rest: RestCode) { }

  setDataByAPI(key: string, api: string, sucFn?: Function, revFn?: Function) {
    const self = this;
    this.dao.getJSON(api).subscribe(
      ret => {
        self.rest.checkCode(ret, retBody => {
          if (sucFn) {
            sucFn(retBody);
          }
          self.setData(key, retBody);
        });
        if (revFn) {
          revFn();
        }
      },
      err => {
        DaoUtil.logError(err);
        if (revFn) {
          revFn();
        }
      }
    );
  }

  addDataByAPI(key: string, api: string, data: any, idx: number, sucFn?: Function, revFn?: Function) {
    const self = this;
    this.dao.postJSON(api, data).subscribe(
      ret => {
        self.rest.checkCode(ret, retBody => {
          if (sucFn) {
            sucFn(retBody);
          }
          self.addData(key, idx, retBody);
        });
        if (revFn) {
          revFn();
        }
      },
      err => {
        DaoUtil.logError(err);
        if (revFn) {
          revFn();
        }
      }
    );
  }

  setHandlingIdx(key: string, idx: number) {
    this.handlingData[key] = idx;
    localStorage.setItem('handlingData', JSON.stringify(this.handlingData));
  }

  getHandlingIdx(key: string) {
    let data = this.handlingData[key];
    if (data !== undefined && data !== null) {
      return data;
    }

    return this.handlingData[key];
  }

  getHandlingData(key: string) {
    let idx = this.handlingData[key];
    if (idx === undefined || idx === null) {
      return null;
    }

    let data = this.lastData[key];
    if (data === undefined || data === null) {
      return null;
    }

    return data[idx];
  }

  getData(key: string): Observable<any> {
    ServiceUtils.makeExist(this.dataSubjects, key, () => new Subject<any>());
    return this.dataSubjects[key].asObservable();
  }

  setData(key: string, data: any) {
    ServiceUtils.makeExist(this.dataSubjects, key, () => new Subject<any>());
    this.lastData[key] = data;
    localStorage.setItem('lastData', JSON.stringify(this.lastData));
    this.dataSubjects[key].next(data);
  }

  addData(key: string, idx: number, data: any) {
    ServiceUtils.makeExist(this.dataSubjects, key, () => new Subject<any>());
    ServiceUtils.makeExist(this.lastData, key, () => []);

    let dataArray = this.lastData[key];
    if (null === idx) {
      dataArray = [data, ...dataArray];
    } else {
      dataArray = [...dataArray.slice(0, idx), data, ...dataArray.slice(idx + 1)];
    }
    this.lastData[key] = dataArray;
    localStorage.setItem('lastData', JSON.stringify(this.lastData));
    this.dataSubjects[key].next(dataArray);
  }

}