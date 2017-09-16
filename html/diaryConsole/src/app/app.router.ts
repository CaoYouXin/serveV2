import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { FallbackComponent } from "./fallback";
import { LoginComponent } from "./login";

import { HomeComponent } from "../diary";

export const routes: Routes = [
  // { path: '', redirectTo: '/index', pathMatch: 'full' },
  { path: '', component: HomeComponent, pathMatch: 'full' },

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