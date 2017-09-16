import { Http, Headers, Response } from "@angular/http";
import { Observable } from "rxjs/Rx";
import "rxjs/add/operator/map";
import { Injectable } from "@angular/core";
import { UserService } from './user.service';

@Injectable()
export class DaoUtil {

  constructor(private http: Http) { }

  getJSON(url: string, header: any = {}): Observable<any> {
    return this.get(url, header).map(res => res.json());
  }

  get(url: string, headers: any = {}): Observable<Response> {
    return this.http.get(url, { headers: DaoUtil.getHeaders(headers) });
  }

  postJSON(url: string, data: any, headers: any = {}): Observable<any> {
    return this.post(url, data, headers).map(res => res.json());
  }

  post(url: string, data: any, headers: any = {}): Observable<Response> {
    return this.http.post(url, data, { headers: DaoUtil.getHeaders(headers) });
  }

  private static getHeaders(userset: any = {}): Headers {
    let headers = new Headers();
    // headers.append('Access-Control-Allow-Origin', `http://${document.domain}:${location.port}`);

    for (var key in userset) {
      if (userset.hasOwnProperty(key)) {
        var element = userset[key];
        headers.append(key, element);
      }
    }

    if (!userset['infinitely-serve-token']) {
      let token = UserService.token();
      if (token) {
        headers.append('infinitely-serve-token', token);
      }
    }

    headers.append('Accept', 'application/json');
    return headers;
  }

  static logError(err) {
    console.log('sth wrong when fetching data. ' + err);
  }

}
