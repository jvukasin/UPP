import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './_helpers/jwt.interceptor';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { HomeComponent } from './home/home.component';
import { RegistrationComponent } from './registration/registration.component';
import { VerifyEmailComponent } from './registration/verify-email/verify-email.component';
import { VerificationDoneComponent } from './verification-done/verification-done.component';
import { AdminComponent } from './admin/admin.component';
import { PotvrdiRecenzentaComponent } from './admin/potvrdi-recenzenta/potvrdi-recenzenta.component';
import { LoginComponent } from './login/login.component';
import { CasopisComponent } from './urednik/urednik.component';
import { DodajOdborComponent } from './urednik/dodaj-odbor-form/dodaj-odbor.component';
import { DodajCasopisComponent } from './urednik/dodaj-casopis-form/dodaj-casopis.component';
import { NavbarComponent } from './navbar/navbar.component';
import { AdminRecenzentiComponent } from './admin/admin-recenzenti/admin-recenzenti.component';
import { AdminUredniciComponent } from './admin/admin-urednici/admin-urednici.component';
import { UrednikCasopisComponent } from './urednik/urednik-casopis/urednik-casopis.component';
import { PrihvatiCasopisComponent } from './admin/prihvati-casopis/prihvati-casopis.component';
import { IspraviCasopisComponent } from './urednik/ispravi-casopis-form/ispravi-casopis.component';
import { NgxSpinnerModule } from "ngx-spinner";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { IspraviOdborComponent } from './urednik/ispravi-odbor-form/ispravi-odbor.component';
import { CasopisListComponent } from './casopis/casopis-list/casopis-list.component';
import { CasopisInfoComponent } from './casopis/casopis-info/casopis-info.component';
import { AutorComponent } from './autor/autor.component';
import { AutorRadComponent } from './autor/autor-rad/autor-rad.component';
import { IzaberiCasopisModalComponent } from './autor/izaberi-casopis-modal/izaberi-casopis-modal.component';
import { DodajRadComponent } from './autor/dodaj-rad/dodaj-rad.component';
import { UplataClanarineComponent } from './autor/uplata-clanarine/uplata-clanarine.component';
import { DodajKoautoreComponent } from './autor/dodaj-koautore/dodaj-koautore.component';
import { UrednikRadComponent } from './urednik/urednik-rad/urednik-rad.component';
import { ObradaRadaComponent } from './urednik/obrada-rada/obrada-rada.component';
import { IspraviRadComponent } from './autor/ispravi-rad/ispravi-rad.component';
import { IzborRecenzenataComponent } from './urednik/izbor-recenzenata/izbor-recenzenata.component';
import { RecenzentComponent } from './recenzent/recenzent.component';
import { RecenzentRadComponent } from './recenzent/recenzent-rad/recenzent-rad.component';
import { RecenziranjeComponent } from './recenzent/recenziranje/recenziranje.component';
import { IzborNovogRecenzentaComponent } from './urednik/izbor-novog-recenzenta/izbor-novog-recenzenta.component';
import { PregledRecenzijaComponent } from './urednik/pregled-recenzija/pregled-recenzija.component';
import { KomIspraviRadComponent } from './autor/kom-ispravi-rad/kom-ispravi-rad.component';
import { FinalIspravkaRadComponent } from './autor/final-ispravka-rad/final-ispravka-rad.component';
import { FinalKomentariPregledComponent } from './urednik/final-komentari-pregled/final-komentari-pregled.component';
import { KorisnikovProfilComponent } from './korisnikov-profil/korisnikov-profil.component';
import { PorudzbineComponent } from './korisnikov-profil/porudzbine/porudzbine.component';
import { PretplateComponent } from './korisnikov-profil/pretplate/pretplate.component';
import { PretragaComponent } from './pretraga/pretraga.component';
import { BasicComponent } from './pretraga/basic/basic.component';
import { AdvancedComponent } from './pretraga/advanced/advanced.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegistrationComponent,
    VerifyEmailComponent,
    VerificationDoneComponent,
    AdminComponent,
    PotvrdiRecenzentaComponent,
    LoginComponent,
    CasopisComponent,
    DodajOdborComponent,
    DodajCasopisComponent,
    NavbarComponent,
    AdminRecenzentiComponent,
    AdminUredniciComponent,
    UrednikCasopisComponent,
    PrihvatiCasopisComponent,
    IspraviCasopisComponent,
    IspraviOdborComponent,
    CasopisListComponent,
    CasopisInfoComponent,
    AutorComponent,
    AutorRadComponent,
    IzaberiCasopisModalComponent,
    DodajRadComponent,
    UplataClanarineComponent,
    DodajKoautoreComponent,
    UrednikRadComponent,
    ObradaRadaComponent,
    IspraviRadComponent,
    IzborRecenzenataComponent,
    RecenzentComponent,
    RecenzentRadComponent,
    RecenziranjeComponent,
    IzborNovogRecenzentaComponent,
    PregledRecenzijaComponent,
    KomIspraviRadComponent,
    FinalIspravkaRadComponent,
    FinalKomentariPregledComponent,
    KorisnikovProfilComponent,
    PorudzbineComponent,
    PretplateComponent,
    PretragaComponent,
    BasicComponent,
    AdvancedComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AngularFontAwesomeModule,
    AppRoutingModule,
    ReactiveFormsModule,
    AppRoutingModule,
    NgxSpinnerModule,
    BrowserAnimationsModule
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }, ],
  bootstrap: [AppComponent]
})
export class AppModule { }
