import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from "@angular/http";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { AppRoutingModule } from './app.router';
import { AppComponent } from './app.component';
import { FallbackComponent } from './fallback';
import { LoginComponent } from './login';
import { AdminService } from '../service';
import { DaoUtil, RestCode } from '../http';

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
    FormsModule,
  ],
  providers: [AdminService, DaoUtil, RestCode],
  bootstrap: [AppComponent]
})
export class AppModule { }
