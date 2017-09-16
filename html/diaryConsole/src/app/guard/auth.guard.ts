import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { RestCode } from "../../http";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (localStorage.getItem('currentUser')) {
      // logged in so return true
      return true;
    }

    // not logged in so redirect to login page with the return url
    RestCode.setLoginRetUrl(state.url);
    this.router.navigate(['/login']);
    return false;
  }
}
