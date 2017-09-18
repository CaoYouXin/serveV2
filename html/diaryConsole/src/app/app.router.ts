import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { FallbackComponent } from "./fallback";
import { LoginComponent } from "./login";

import {
  HomeComponent, BookComponent, PageComponent, NewPageComponent,
  EditBookComponent, EidtBookPageComponent, MilestoneComponent
} from "../diary";

import { AuthGuard } from "./guard";

export const routes: Routes = [
  // { path: '', redirectTo: '/index', pathMatch: 'full' },
  { path: '', component: HomeComponent, pathMatch: 'full' },

  { path: 'book', component: BookComponent, canActivate: [AuthGuard] },
  { path: 'page', component: PageComponent, canActivate: [AuthGuard] },
  { path: 'editpage', component: NewPageComponent, canActivate: [AuthGuard] },
  { path: 'editbook', component: EditBookComponent, canActivate: [AuthGuard] },
  { path: 'editbookpage', component: EidtBookPageComponent, canActivate: [AuthGuard] },
  { path: 'milestone', component: MilestoneComponent, canActivate: [AuthGuard] },

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