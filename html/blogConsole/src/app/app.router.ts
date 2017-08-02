import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AuthGuard, LoginResolver} from "./guard/index";
import {
  HomeComponent,
  LoginComponent,
} from "./component/index";
import {TestComponent} from "./test/test.component";
import {TabsComponent} from "./tabs/tabs.component";

export const routes: Routes = [
  {path: 'never-match', component: HomeComponent, data: {name: '菜单'}},
  {path: '', component: HomeComponent, data: {name: 'Home'}},
  {path: 'red', component: TabsComponent, data: {name: 'Test1', color: 'red', children: ['blue', 'green']},
    children: [
      {
        path: 'yellow',
        component: TestComponent,
        data: {name: "Test1-1", color: 'yellow'}
      },
      {
        path: 'green',
        component: TestComponent,
        data: {name: "Test1-2", color: 'green'}
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
