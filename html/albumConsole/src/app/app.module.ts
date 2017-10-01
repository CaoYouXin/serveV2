import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { FallbackComponent } from "./fallback.component";
import { AppRoutingModule } from './app.router';
import { HomeComponent } from './home/home.component';
import { LoginedGuard } from "./guards/logined.guard";
import { UserService } from './services/user.service';
import { UserModule } from './user/user.module';

@NgModule({
  declarations: [
    AppComponent,
    FallbackComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    UserModule
  ],
  providers: [UserService, LoginedGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
