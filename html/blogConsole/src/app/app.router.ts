import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent, LoginComponent,} from "./component/index";
import {TestComponent} from "./test/test.component";
import {TabsComponent} from "./tabs/tabs.component";

export const routes: Routes = [
  {path: '', component: HomeComponent, data: {name: 'Home'}},
  {
    path: 'resource', component: TabsComponent, data: {name: '资源管理'},
    children: [
      {
        path: 'types',
        component: TestComponent,
        data: {name: "资源类型管理", color: 'yellow'}
      },
      {
        path: 'mapping',
        component: TestComponent,
        data: {name: "资源-文件映射管理", color: 'green'}
      }
    ]
  },
  {path: 'user', component: TestComponent, data: {name: '用户管理', color: 'greenyellow', width: '80%'}},
  {
    path: 'favour', component: TabsComponent, data: {name: '好感度管理'},
    children: [
      {
        path: 'list',
        component: TestComponent,
        data: {name: "用户好感度列表", color: 'yellow'}
      },
      {
        path: 'mapping',
        component: TestComponent,
        data: {name: "好感度-资源映射管理", color: 'green'}
      }
    ]
  },
  {
    path: 'post', component: TabsComponent, data: {name: 'POST 管理'},
    children: [
      {
        path: 'category',
        component: TestComponent,
        data: {name: "分类管理", color: 'yellow'}
      },
      {
        path: 'post',
        component: TestComponent,
        data: {name: "Post 管理", color: 'green'}
      },
      {
        path: 'comment',
        component: TestComponent,
        data: {name: "评论管理", color: 'indianred'}
      }
    ]
  },
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
