import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {
  HTTP_INTERCEPTORS,
  HttpClient,
  HttpClientModule,
  provideHttpClient,
  withInterceptorsFromDi
} from "@angular/common/http";
import { RegisterComponent } from './pages/register/register.component';
import { ActivateAccountComponent } from './pages/activate-account/activate-account.component';
import {CodeInputModule} from "angular-code-input";
import {HttpTokenInterceptor} from "./services/interceptor/http-token.interceptor";
import {ApiModule} from "./services/api.module";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ActivateAccountComponent
  ],
  imports: [

    BrowserModule,
    AppRoutingModule,
    FormsModule,
    NgForOf,
    NgIf,
    CodeInputModule,
    ApiModule.forRoot({rootUrl: 'http://VPS_ADRESS:8088/api/v1'}),
  ],
  providers: [
    provideHttpClient(withInterceptorsFromDi()),
    //Pour ajouter un interceptor de requête HTTP pour intégrer
    // le token d'authentification dans les requêtes sortantes
    {
      provide: HTTP_INTERCEPTORS ,
      useClass : HttpTokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
