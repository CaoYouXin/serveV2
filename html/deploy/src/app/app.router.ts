import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AuthGuard, InitResolver} from "./guard/index";
import {HomeComponent} from "./home/index";
import {LoginComponent} from "./login/index";
import {AdminSettingComponent} from "./setting.admin/index";
import {DBSettingComponent} from "./setting.db/index";

export const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard], resolve: {init: InitResolver}, data: {name: 'Home'} },
  { path: 'login', component: LoginComponent, resolve: {init: InitResolver} },
  { path: 'setting/admin', component: AdminSettingComponent },
  { path: 'setting/db', component: DBSettingComponent},

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
