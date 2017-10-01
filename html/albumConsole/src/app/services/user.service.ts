import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { API } from './api.const';
import { Observable } from "rxjs";
import { Subject } from 'rxjs/Subject';

@Injectable()
export class UserService {

  static _url: string;

  userNameSub: Subject<string> = new Subject<string>();

  constructor(private http: HttpService) { }

  tokenExist() {
    return Boolean(localStorage.getItem('currentUser'));
  }

  setRetUrl(url) {
    UserService._url = url;
  }

  getRetUrl() {
    return UserService._url || '/';
  }

  saveCurrentUser(currentUser) {
    localStorage.setItem('currentUser', JSON.stringify(currentUser));
  }

  bindUserName(): Observable<string> {
    return this.userNameSub.asObservable();
  }

  refreshUserName(userName?: string) {
    if (userName !== undefined) {
      this.userNameSub.next(userName);
      return;
    }

    const currentUserJson = localStorage.getItem('currentUser');
    if (!currentUserJson) {
      this.userNameSub.next('');
      return;
    }

    const currentUser = JSON.parse(currentUserJson);
    this.userNameSub.next(currentUser.UserName);
  }

  login(model, suc, fail) {
    this.http.postJSON(API.getAPI("login"), model)
      .subscribe(ret => {
        this.saveCurrentUser(ret);
        this.refreshUserName(ret.UserName);
        suc(ret);
      }, err => fail(err));
  }

  logout() {
    localStorage.setItem('currentUser', '');
    this.refreshUserName('');
  }

  register(model, suc, fail) {
    this.http.postJSON(API.getAPI("register"), model)
      .subscribe(ret => suc(ret), err => fail(err));
  }

}
