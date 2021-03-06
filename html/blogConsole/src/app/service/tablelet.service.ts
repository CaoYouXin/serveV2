import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Subject} from "rxjs/Subject";

@Injectable() 
export class TableletService {

  static RESOURCE_MANAGER = "RESOURCE_MANAGER";
  static RESOURCE_MAPPING = "RESOURCE_MAPPING";
  static BLOG_USER = "BLOG_USER";
  static USER_FAVOUR = "USER_FAVOUR";
  static FAVOUR_MAPPING = "FAVOUR_MAPPING";
  static FAVOUR_RULE = "FAVOUR_RULE";
  static BLOG_CATEGORY = "BLOG_CATEGORY";
  static BLOG_POST = "BLOG_POST";

  private maskStatusSubjects = {};
  private dataSubjects = {};
  private lastData = {};

  getMaskStatus(key: string): Observable<any> {
    this.makeExist(this.maskStatusSubjects, key, () => new Subject<any>());
    return this.maskStatusSubjects[key].asObservable();
  }

  setMaskStatus(key: string, status: any) {
    this.makeExist(this.maskStatusSubjects, key, () => new Subject<any>());
    this.maskStatusSubjects[key].next(status);
  }

  getData(key: string): Observable<any> {
    this.makeExist(this.dataSubjects, key, () => new Subject<any>());
    return this.dataSubjects[key].asObservable();
  }

  setData(key: string, data: any) {
    this.makeExist(this.dataSubjects, key, () => new Subject<any>());
    this.lastData[key] = data;
    this.dataSubjects[key].next(data);
  }

  addData(key: string, idx: number, data: any) {
    this.makeExist(this.dataSubjects, key, () => new Subject<any>());
    this.makeExist(this.lastData, key, () => []);

    let dataArray = this.lastData[key];
    if (null === idx) {
      dataArray = [data, ...dataArray];
    } else {
      dataArray = [...dataArray.slice(0, idx), data, ...dataArray.slice(idx + 1)];
    }
    this.lastData[key] = dataArray;
    this.dataSubjects[key].next(dataArray);
  }

  private makeExist(subjects: any, key: string, cb) {
    if (!subjects[key]) {
      subjects[key] = cb();
    }
  }

}