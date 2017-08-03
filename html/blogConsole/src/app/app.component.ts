import { Component } from "@angular/core";
import { Routes } from "@angular/router";
import { routes as appRoutes } from "./app.router";
import { BlogService } from "./service/index";
import { DaoUtil, RestCode } from "./http/index";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css', './common-style/button.component.css']
})
export class AppComponent {

  routes: Routes = appRoutes.filter(route => {
    return route.data;
  });

  constructor(private service: BlogService,
    private restCode: RestCode) { }

  deployExt() {
    this.service.init().subscribe(
      ret => this.restCode.checkCode(ret, (ret) => {
        if (ret.error) {
          return;
        }

        alert(ret);
      }),
      err => DaoUtil.logError(err)
    );
  }

}
