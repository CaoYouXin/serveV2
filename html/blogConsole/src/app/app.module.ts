import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {RestCode} from "./const/index";
import {DaoUtil} from "caols-common-modules";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app.router";
import {
  AppRoutingComponent,
  HomeComponent,
  LoginComponent,
  TableComponent,
} from "./component/index";
import {AuthGuard, LoginResolver} from "./guard/index";
import {
  AdminService,
  RouteService,
} from "./service/index";

@NgModule({
  declarations: [
    AppComponent,
    AppRoutingComponent,
    HomeComponent,
    LoginComponent,
    TableComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [
    RestCode,
    DaoUtil,
    AuthGuard,
    LoginResolver,
    RouteService,
    AdminService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
