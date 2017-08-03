import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Subject} from "rxjs/Subject";

@Injectable() 
export class ResourceManangerService {
  private subject = new Subject<boolean>();

  getMaskStatus(): Observable<boolean> {
    return this.subject.asObservable();
  }

  setMaskStatus(status: boolean) {
    this.subject.next(status);
  }
}