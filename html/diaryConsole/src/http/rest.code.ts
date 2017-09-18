import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable()
export class RestCode {

  private static loginRetUrl: string;

  constructor(private router: Router) {
  }

  checkCode(ret, cb) {
    if (ret.code === 50000) {
      alert(ret.body);
      return;
    }

    if (ret.code === 50001) {
      RestCode.loginRetUrl = location.href.toString();
      this.router.navigate(['/login']);
      return;
    }

    if (ret.code !== 20000) {
      ret.body.error = ret.code;
      return;
    }

    cb(ret.body);
  }

  static setLoginRetUrl(): void {
    RestCode.loginRetUrl = location.href.toString();
  }

  static getLoginRetUrl(): string {
    return RestCode.loginRetUrl;
  }

}
