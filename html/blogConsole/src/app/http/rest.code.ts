import {Injectable} from "@angular/core";
import {Router} from "@angular/router";

@Injectable()
export class RestCode {

  constructor(private router: Router) {
  }

  checkCode(ret, cb) {
    if (ret.code === 50000) {
      alert(ret.body);
      return;
    }

    if (ret.code === 50001) {
      this.router.navigate(['/login']);
      return;
    }

    if (ret.code !== 20000) {
      ret.body.error = ret.code;
      return;
    }

    cb(ret.body);
  }

}
