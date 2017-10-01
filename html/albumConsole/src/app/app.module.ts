import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { FallbackComponent } from "./fallback.component";
import { AppRoutingModule } from './app.router';

@NgModule({
  declarations: [
    AppComponent,
    FallbackComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
