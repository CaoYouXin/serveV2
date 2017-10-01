import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { API } from './api.const';

@Injectable()
export class UserService {

  static _url: string;

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

  login(model, suc, fail) {
    this.http.postJSON(API.getAPI("login"), model)
      .subscribe(ret => suc(ret), err => fail(err));
  }

}
