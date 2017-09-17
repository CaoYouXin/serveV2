import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Subject } from "rxjs/Subject";
import { ServiceUtils } from "./service.util";

@Injectable()
export class TableletService {

  static PAGEs = "PAGEs";
  static BOOKs = "BOOKs";
  // static MILESTONEs = "MILESTONEs";

  private dataSubjects = {};
  private lastData = {};

  getData(key: string): Observable<any> {
    ServiceUtils.makeExist(this.dataSubjects, key, () => new Subject<any>());
    return this.dataSubjects[key].asObservable();
  }

  setData(key: string, data: any) {
    ServiceUtils.makeExist(this.dataSubjects, key, () => new Subject<any>());
    this.lastData[key] = data;
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
    this.dataSubjects[key].next(dataArray);
  }

}