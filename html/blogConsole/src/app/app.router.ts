import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AuthGuard, LoginResolver} from "./guard/index";
import {
  HomeComponent,
  LoginComponent,
} from "./component/index";

export const routes: Routes = [
  {path: 'never-match', component: HomeComponent, data: {name: '菜单'}},
  {path: '', component: HomeComponent, data: {name: 'Home'}},
  {path: '', component: HomeComponent, data: {name: 'Home'}},
  {path: '', component: HomeComponent, data: {name: 'Home'}},
  {path: '', component: HomeComponent, data: {name: 'Home'}},
  {path: 'login', component: LoginComponent},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
