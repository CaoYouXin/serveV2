import {Component} from "@angular/core";
import {Route, Routes} from "@angular/router";
import {routes as appRoutes} from "./app.router";
import {DaoUtil} from "caols-common-modules";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  routes: Routes = appRoutes.filter(route => {
    return route.data;
  });

}
