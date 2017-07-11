import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
// import "rxjs/add/operator/toPromise";
import {DatabaseService} from "../service/index";

@Injectable()
export class DBStatusResolver implements Resolve<any> {

  constructor(private service: DatabaseService){
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> | any {
    return this.service.status();
  }

}
