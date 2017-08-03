import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Subject} from "rxjs/Subject";
import {NavigationCancel, NavigationEnd, Router} from "@angular/router";

@Injectable()
export class RouteService {
  private subject = new Subject<any>();
  private currentRouteConfig = new Subject<any>();

  constructor(private router: Router) {
    const self = this;
    router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        self.subject.next({
          url: event.url,
          seleted: true
        });
      }
      if (event instanceof NavigationCancel) {
        self.subject.next({
          url: event.url,
          seleted: false
        });
      }
    });
  }

  getRoute(): Observable<any> {
    return this.subject.asObservable();
  }

  getCurrentRouteConfig(): Observable<any> {
    return this.currentRouteConfig.asObservable();
  }

  setCurrentRouteConfig(routeConfig) {
    this.currentRouteConfig.next(routeConfig);
  }
}
