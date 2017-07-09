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
  ControllerComponent,
  DBSettingComponent,
  HomeComponent,
  LoginComponent,
  MicroServiceComponent,
  RestartComponent,
  UploadCodeComponent,
  UploadComponent,
  UploadUtil
} from "./component/index";
import {AuthGuard, InitResolver} from "./guard/index";
import {AdminService, DatabaseService, FileService, RouteService, ServerService} from "./service/index";

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
    UploadCodeComponent
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
    FileService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
