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

export const routes: Routes = [
  { path: '', component: HomeComponent, data: { name: 'Home' } },
  {
    path: 'resource', component: TabsComponent, data: { name: '资源管理' },
    children: [
      { path: 'types', component: ResourceManagerComponent, data: { name: "资源类型管理" } },
      { path: 'mapping', component: ResourceMappingComponent, data: { name: "资源-文件映射管理" } },
      { path: 'upload', component: ResourceUploadComponent, data: { name: "资源上传管理" } },
      { path: 'deploy', component: ResourceDeployComponent, data: { name: "资源部署管理" } }
    ]
  },
  { path: 'user', component: BlogUserComponent, data: { name: '用户管理' } },
  {
    path: 'favour', component: TabsComponent, data: { name: '好感度管理' },
    children: [
      { path: 'list', component: FavourDetailComponent, data: { name: "用户好感度列表" } },
      { path: 'mapping', component: FavourMappingComponent, data: { name: "好感度-资源映射管理" } },
      { path: 'rule', component: FavourRuleComponent, data: { name: "好感度规则管理" } }
    ]
  },
  {
    path: 'post', component: TabsComponent, data: { name: 'POST 管理' },
    children: [
      { path: 'category', component: BlogCategoryComponent, data: { name: "分类管理" } },
      { path: 'post', component: BlogPostComponent, data: { name: "Post 管理" } },
      { path: 'comment', component: BlogCommentComponent, data: { name: "评论管理" } }
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
