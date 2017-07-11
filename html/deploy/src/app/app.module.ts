import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";

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
  TableComponent,
  UploadCodeComponent,
  UploadComponent,
  UploadUtil
} from "./component/index";
import {AuthGuard, InitResolver} from "./guard/index";
import {
  AdminService,
  ControllerService,
  DatabaseService,
  FileService,
  RouteService,
  ServerService,
  ServiceService
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
    TableComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [
    DaoUtil,
    UploadUtil,
    AuthGuard,
    InitResolver,
    RouteService,
    DatabaseService,
    AdminService,
    ServerService,
    FileService,
    ControllerService,
    ServiceService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
