import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { LoginedGuard } from "./guards/logined.guard";

import { FallbackComponent } from "./fallback.component";
import { HomeComponent } from "./home/home.component";
import { LoginComponent } from "./user/login/login.component";
import { RegisterComponent } from "./user/register/register.component";

import { AlbumComponent } from "./album/album/album.component";
import { PhotoComponent } from "./album/photo/photo.component";

export const routes: Routes = [
  // { path: '', redirectTo: '/index', pathMatch: 'full' },
  { path: '', component: HomeComponent, canActivate: [LoginedGuard], pathMatch: 'full' },

  {
    path: 'albums', component: AlbumComponent, canActivate: [LoginedGuard],
    children: [
      { path: ':id', component: FallbackComponent, canActivate: [LoginedGuard], data: { name: 'hello world', color: '#f321ff' } }
    ]
  },
  { path: 'photos', component: PhotoComponent, canActivate: [LoginedGuard] },

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

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