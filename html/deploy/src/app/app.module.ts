import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {DaoUtil} from "caols-common-modules";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app.router";
import {
  AdminSettingComponent,
  DBSettingComponent,
  HomeComponent,
  LoginComponent,
  RestartComponent,
  AppRoutingComponent,
  UploadComponent,
  MicroServiceComponent,
  ControllerComponent,
  UploadUtil
} from "./component/index";
import {AuthGuard, InitResolver} from "./guard/index";
import {AdminService, DatabaseService, RouteService, ServerService} from "./service/index";

@NgModule({
  declarations: [
    AppComponent,
    AppRoutingComponent,
    HomeComponent,
    LoginComponent,
    AdminSettingComponent,
    DBSettingComponent,
    RestartComponent,
    UploadComponent,
    MicroServiceComponent,
    ControllerComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [DaoUtil, UploadUtil, AuthGuard, InitResolver, RouteService, DatabaseService, AdminService, ServerService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
