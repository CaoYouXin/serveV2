import { Injectable } from '@angular/core';

@Injectable()
export class UserService {

  static _url: string;

  constructor() { }

  tokenExist() {
    return Boolean(localStorage.getItem('user-token'));
  }

  setRetUrl(url) {
    UserService._url = url;
    console.log(UserService._url);
  }

}
