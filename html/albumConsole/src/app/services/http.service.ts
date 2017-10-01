import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Http, Headers, Response } from "@angular/http";
import { Observable } from "rxjs/Rx";
import "rxjs/add/operator/map";

@Injectable()
export class HttpService {

  constructor(private http: Http, private router: Router) { }

  getJSON(url: string, header: any = {}): Observable<any> {
    return this.get(url, header).map(res => res.json()).map(this.checkCode);
  }

  get(url: string, headers: any = {}): Observable<Response> {
    return this.http.get(url, { headers: this.getHeaders(headers) });
  }

  postJSON(url: string, data: any, headers: any = {}): Observable<any> {
    return this.post(url, data, headers).map(res => res.json()).map(this.checkCode);
  }

  post(url: string, data: any, headers: any = {}): Observable<Response> {
    return this.http.post(url, data, { headers: this.getHeaders(headers) });
  }

  getHeaders(userset: any = {}): Headers {
    let headers = new Headers();
    // headers.append('Access-Control-Allow-Origin', `http://${document.domain}:${location.port}`);

    for (var key in userset) {
      if (userset.hasOwnProperty(key)) {
        var element = userset[key];
        headers.append(key, element);
      }
    }

    if (!userset['infinitely-serve-token']) {
      let currentUser = localStorage.getItem('currentUser');
      if (currentUser) {
        currentUser = JSON.parse(currentUser);
        let date: Date = new Date(currentUser['UserTokenDeadTime']);
        if (date.getTime() - Date.now() > 0) {
          headers.append('infinitely-serve-token', currentUser['UserToken']);
        } else {
          this.router.navigateByUrl('/login');
        }
      }
    }

    headers.append('Accept', 'application/json');
    return headers;
  }

  checkCode(ret) {
    if (ret.code === 50001) {
      this.router.navigateByUrl('/login');
    }

    if (ret.code !== 20000) {
      alert('发生[' + ret.code + ']错误，日志：' + ret.body);
      throw ret;
    }

    return ret.body;
  }

}
