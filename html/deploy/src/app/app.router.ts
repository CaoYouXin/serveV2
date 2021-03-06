import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AuthGuard, DBStatusResolver, InitResolver, LoginResolver} from "./guard/index";
import {
  AdminSettingComponent,
  ControllerComponent,
  DBSettingComponent,
  HomeComponent,
  LoginComponent,
  MicroServiceComponent,
  RestartComponent,
  ServeAuthComponent,
  UploadComponent,
  InterceptorComponent
} from "./component/index";

export const routes: Routes = [
  {path: '', component: HomeComponent, data: {name: 'Home'}},
  {path: 'upload', component: UploadComponent, canActivate: [AuthGuard], data: {name: '代码'}},
  {path: 'controller', component: ControllerComponent, canActivate: [AuthGuard], data: {name: '控制器'}},
  {path: 'interceptor', component: InterceptorComponent, canActivate: [AuthGuard], data: {name: '拦截器'}},
  {path: 'microservice', component: MicroServiceComponent, canActivate: [AuthGuard], data: {name: '微服务'}},
  {path: 'serve/auth', component: ServeAuthComponent, canActivate: [AuthGuard], data: {name: '资源验证'}},
  {path: 'login', component: LoginComponent, resolve: {init: InitResolver}},
  {path: 'setting/admin', component: AdminSettingComponent, resolve: {init: InitResolver}, data: {name: '管理员'}},
  {
    path: 'setting/db',
    component: DBSettingComponent,
    resolve: {isLogin: LoginResolver, status: DBStatusResolver},
    data: {name: '数据库'}
  },
  {path: 'server/restart', component: RestartComponent, canActivate: [AuthGuard], data: {name: '服务器'}},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
