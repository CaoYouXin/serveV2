import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from "@angular/http";

import { AppRoutingModule } from './app.router';
import { UserModule } from './user/user.module';
import { AlbumModule } from './album/album.module';
import { VideoModule } from './video/video.module';

import { AppComponent } from './app.component';
import { FallbackComponent } from "./fallback.component";
import { HomeComponent } from './home/home.component';
import { LoginedGuard } from "./guards/logined.guard";
import { UserService } from './services/user.service';
import { HttpService } from './services/http.service';

@NgModule({
  declarations: [
    AppComponent,
    FallbackComponent,
    HomeComponent
  ],
  imports: [
    HttpModule,
    BrowserModule,
    AppRoutingModule,
    UserModule,
    AlbumModule,
    VideoModule
  ],
  providers: [HttpService, UserService, LoginedGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
