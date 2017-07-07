import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot} from "@angular/router";
import {AdminService, DatabaseService} from "../service/index";
import "rxjs/add/operator/toPromise";

@Injectable()
export class InitResolver implements Resolve<any> {
  constructor(private router: Router,
              private databaseService: DatabaseService,
              private adminService: AdminService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<any> {

    if (route.url.length == 2
      && 'setting' === route.url[0].path
      && 'db' === route.url[1].path) {
      return Promise.resolve(false);
    }

    return this.databaseService.status().toPromise().then(
      ret => {
        if (ret.ActiveDatasource) {
          if (route.url.length == 2
            && 'setting' === route.url[0].path
            && 'admin' === route.url[1].path) {
            return Promise.resolve(false);
          }

          return this.adminService.check().toPromise().then(
            ret => {
              if (ret) {
                return true;
              }

              this.router.navigate(['/setting/admin']);
              return false;
            }
          );
        }

        this.router.navigate(['/setting/db']);
        return false;
      }
    );
  }
}
