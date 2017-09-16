import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import {
  HomeComponent,
  LoginComponent,
  TabsComponent,
  ResourceManagerComponent,
  ResourceMappingComponent,
  ResourceUploadComponent,
  ResourceDeployComponent,
  BlogUserComponent,
  FavourDetailComponent,
  FavourMappingComponent,
  FavourRuleComponent,
  BlogCategoryComponent,
  BlogPostComponent,
  BlogCommentComponent
} from "./component/index";
import { TestComponent } from "./test/test.component";
import { AuthGuard } from "./guard";

export const routes: Routes = [
  { path: '', component: HomeComponent, data: { name: 'Home' } },
  {
    path: 'resource', component: TabsComponent, data: { name: '资源管理' }, canActivate: [AuthGuard],
    children: [
      { path: 'types', component: ResourceManagerComponent, canActivate: [AuthGuard], data: { name: "资源类型管理" } },
      { path: 'mapping', component: ResourceMappingComponent, canActivate: [AuthGuard], data: { name: "资源-文件映射管理" } },
      { path: 'upload', component: ResourceUploadComponent, canActivate: [AuthGuard], data: { name: "资源上传管理" } },
      { path: 'deploy', component: ResourceDeployComponent, canActivate: [AuthGuard], data: { name: "资源部署管理" } }
    ]
  },
  { path: 'user', component: BlogUserComponent, canActivate: [AuthGuard], data: { name: '用户管理' } },
  {
    path: 'favour', component: TabsComponent, data: { name: '好感度管理' }, canActivate: [AuthGuard],
    children: [
      { path: 'list', component: FavourDetailComponent, canActivate: [AuthGuard], data: { name: "用户好感度列表" } },
      { path: 'mapping', component: FavourMappingComponent, canActivate: [AuthGuard], data: { name: "好感度-资源映射管理" } },
      { path: 'rule', component: FavourRuleComponent, canActivate: [AuthGuard], data: { name: "好感度规则管理" } }
    ]
  },
  {
    path: 'post', component: TabsComponent, data: { name: 'POST 管理' }, canActivate: [AuthGuard],
    children: [
      { path: 'category', component: BlogCategoryComponent, canActivate: [AuthGuard], data: { name: "分类管理" } },
      { path: 'post', component: BlogPostComponent, canActivate: [AuthGuard], data: { name: "Post 管理" } },
      { path: 'comment', component: BlogCommentComponent, canActivate: [AuthGuard], data: { name: "评论管理" } }
    ]
  },
  { path: 'login', component: LoginComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
