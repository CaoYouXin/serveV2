import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {DaoUtil} from "caols-common-modules";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app.router";
import {HomeComponent} from "./home/index";
import {LoginComponent} from "./login/index";
import {AdminSettingComponent} from "./setting.admin/index";
import {DBSettingComponent} from "./setting.db/index";
import {AppRoutingComponent} from "./route/index";
import {AuthGuard, InitResolver} from "./guard/index";
import {AdminService, DatabaseService, RouteService} from "./service/index";

@NgModule({
  declarations: [
    AppComponent,
    AppRoutingComponent,
    HomeComponent,
    LoginComponent,
    AdminSettingComponent,
    DBSettingComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [DaoUtil, AuthGuard, InitResolver, RouteService, DatabaseService, AdminService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
