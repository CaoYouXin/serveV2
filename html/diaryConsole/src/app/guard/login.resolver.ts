import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";

@Injectable()
export class LoginResolver implements Resolve<boolean> {

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> | any {
    return (localStorage.getItem("currentUser") && true) || false;
  }

}
