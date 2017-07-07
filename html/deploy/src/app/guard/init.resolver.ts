import {Injectable}             from '@angular/core';
import {
  Router, Resolve, RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {DatabaseService, AdminService} from "../service/index";
import 'rxjs/add/operator/toPromise';

@Injectable()
export class InitResolver implements Resolve<any> {
  constructor(private router: Router,
              private databaseService: DatabaseService,
              private adminService: AdminService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<any> {

    return this.databaseService.status().toPromise().then(
      ret => {
        if (ret.ActiveDatasource) {
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
