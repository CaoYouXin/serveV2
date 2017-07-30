import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AdminService} from "../service/index";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private service: AdminService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (localStorage.getItem('currentUser')) {
      // logged in so return true
      return true;
    }

    // not logged in so redirect to login page with the return url
    this.service.setReturnUrl(state.url);
    this.router.navigate(['/login']);
    return false;
  }
}
