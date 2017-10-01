import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { LoginedGuard } from "./guards/logined.guard";

import { FallbackComponent } from "./fallback.component";
import { HomeComponent } from "./home/home.component";
import { LoginComponent } from "./user/login/login.component";

export const routes: Routes = [
  // { path: '', redirectTo: '/index', pathMatch: 'full' },
  { path: '', component: HomeComponent, canActivate: [LoginedGuard], pathMatch: 'full' },

  { path: 'albums', component: HomeComponent, canActivate: [LoginedGuard] },
  { path: 'photos', component: HomeComponent, canActivate: [LoginedGuard] },

  { path: 'login', component: LoginComponent },

  { path: '**', component: FallbackComponent, data: { name: '看上去，你是迷路了吧？', color: '#EADFCB' } }
];

@NgModule({
  // imports: [RouterModule.forRoot(routes, {useHash: true})],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}