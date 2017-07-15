import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class RestCode {

  constructor(private router: Router) {
  }

  checkCode(ret, cb) {
    if (ret.code === 50000) {
      alert(ret.body);
    }

    if (ret.code === 50001) {
      this.router.navigate(['/login']);
    }

    if (ret.code !== 20000) {
      ret.body.error = ret.code;
      cb(ret.body);
    }

    cb(ret.body);
  }

}
