import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { RestCode, DaoUtil } from "./http/index";
import { AppComponent } from "./app.component";
import { AppRoutingModule } from "./app.router";
import {
  AppRoutingComponent,
  HomeComponent,
  LoginComponent,
  TableComponent,
  TabsComponent,
  SoundComponent,
  UploadCodeComponent,
  DeployDstComponent,
  DeploySrcComponent,
  ResourceManagerComponent,
  ResourceManangerMaskComponent,
  ResourceMappingComponent,
  ResourceMappingMaskComponent,
  ResourceUploadComponent,
  ResourceDeployComponent,
  BlogUserComponent,
  FavourDetailComponent,
  FavourDetialMaskComponent,
  FavourMappingComponent,
  FavourMappingMaskComponent,
  FavourRuleComponent,
  FavourRuleMaskComponent,
  BlogCategoryComponent,
  BlogCategoryMaskComponent,
  BlogCategoryPreviewComponent,
  BlogCategoryItemComponent,
  BlogPostComponent,
  BlogPostMaskComponent,
  BlogScreenshotComponent,
  BlogCommentComponent,
  ContentButtonComponent
} from "./component/index";
import { AuthGuard, LoginResolver } from "./guard/index";
import {
  AdminService,
  RouteService,
  BlogService,
  TableletService,
  FileService,
  UploadService,
  SoundService
} from "./service/index";
import { TestComponent } from "./test/test.component";

@NgModule({
  declarations: [
    AppComponent,
    AppRoutingComponent,
    HomeComponent,
    LoginComponent,
    TableComponent,
    SoundComponent,
    TestComponent,
    TabsComponent,
    UploadCodeComponent,
    DeployDstComponent,
    DeploySrcComponent,
    ResourceManagerComponent,
    ResourceManangerMaskComponent,
    ResourceMappingComponent,
    ResourceMappingMaskComponent,
    ResourceUploadComponent,
    ResourceDeployComponent,
    BlogUserComponent,
    FavourDetailComponent,
    FavourDetialMaskComponent,
    FavourMappingComponent,
    FavourMappingMaskComponent,
    FavourRuleComponent,
    FavourRuleMaskComponent,
    BlogCategoryComponent,
    BlogCategoryMaskComponent,
    BlogCategoryPreviewComponent,
    BlogCategoryItemComponent,
    BlogPostComponent,
    BlogPostMaskComponent,
    BlogScreenshotComponent,
    BlogCommentComponent,
    ContentButtonComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
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
    BlogService,
    TableletService,
    FileService,
    UploadService,
    SoundService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
