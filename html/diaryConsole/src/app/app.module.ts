import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from "@angular/http";
import { ReactiveFormsModule } from "@angular/forms";

import { AppRoutingModule } from './app.router';
import { DiaryModule } from '../diary';

import { AppComponent } from './app.component';
import { FallbackComponent } from './fallback';
import { LoginComponent } from './login';
import { AdminService } from '../service';
import { DaoUtil, RestCode } from '../http';
import { AuthGuard } from './guard';

@NgModule({
  declarations: [
    AppComponent,
    FallbackComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    AppRoutingModule,
    ReactiveFormsModule,
    DiaryModule
  ],
  providers: [AdminService, DaoUtil, RestCode, AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
