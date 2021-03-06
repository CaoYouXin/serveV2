import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

import {RestCode} from "./const/index";
import {DaoUtil} from "caols-common-modules";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app.router";
import {
  AdminSettingComponent,
  AppRoutingComponent,
  CodeComponent,
  ControllerComponent,
  DBSettingComponent,
  HomeComponent,
  LoginComponent,
  MicroServiceComponent,
  RestartComponent,
  ServeAuthComponent,
  TableComponent,
  UploadCodeComponent,
  UploadComponent,
  InterceptorComponent,
  UploadUtil
} from "./component/index";
import {AuthGuard, DBStatusResolver, InitResolver, LoginResolver} from "./guard/index";
import {
  AdminService,
  ControllerService,
  DatabaseService,
  FileService,
  RouteService,
  ServeAuthService,
  ServerService,
  ServiceService,
  InterceptorService
} from "./service/index";

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
    ControllerComponent,
    UploadCodeComponent,
    CodeComponent,
    TableComponent,
    ServeAuthComponent,
    InterceptorComponent
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
    UploadUtil,
    AuthGuard,
    InitResolver,
    DBStatusResolver,
    LoginResolver,
    RouteService,
    DatabaseService,
    AdminService,
    ServerService,
    FileService,
    ControllerService,
    ServiceService,
    ServeAuthService,
    InterceptorService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
