import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';
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
import { AdminRecenzentiComponent } from './admin/admin-recenzenti/admin-recenzenti.component';
import { AdminUredniciComponent } from './admin/admin-urednici/admin-urednici.component';
import { UrednikCasopisComponent } from './urednik/urednik-casopis/urednik-casopis.component';
import { PrihvatiCasopisComponent } from './admin/prihvati-casopis/prihvati-casopis.component';
import { IspraviCasopisComponent } from './urednik/ispravi-casopis-form/ispravi-casopis.component';
import { IspraviOdborComponent } from './urednik/ispravi-odbor-form/ispravi-odbor.component';
import { CasopisListComponent } from './casopis/casopis-list/casopis-list.component';
import { CasopisInfoComponent } from './casopis/casopis-info/casopis-info.component';
import { AutorComponent } from './autor/autor.component';
import { AutorRadComponent } from './autor/autor-rad/autor-rad.component';


const appRoutes: Routes = [
	{ path: '', redirectTo: '/pocetna/casopisi', pathMatch: 'full' },
    { path: 'pocetna', component: HomeComponent , children: [
		{ path: '', redirectTo: 'casopisi', pathMatch: 'full'},
		{ path: 'casopisi', component: CasopisListComponent},
		{ path: 'casopisi/:id', component: CasopisInfoComponent},
	]},
	{ path: 'registration', component: RegistrationComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'verify', component: VerifyEmailComponent },
	{ path: 'verified', component: VerificationDoneComponent },
	{ path: 'admin', component: AdminComponent, children: [
		{ path: '', redirectTo: 'recenzenti', pathMatch: 'full'},
		{ path: 'recenzenti', component: AdminRecenzentiComponent},
		{ path: 'urednici', component: AdminUredniciComponent},
	]},
	{ path: 'admin/:id', component: PotvrdiRecenzentaComponent },
	{ path: 'admin/casopis/:id', component: PrihvatiCasopisComponent },
	{ path: 'urednik', component: CasopisComponent, children: [
		{ path: '', redirectTo: 'casopisi', pathMatch: 'full'},
		{ path: 'casopisi', component: UrednikCasopisComponent},
	]},
	{ path: 'dodaj/casopis', component: DodajCasopisComponent },
	{ path: 'casopis/:id', component: DodajOdborComponent },
	{ path: 'casopis/promeni/:id', component: IspraviCasopisComponent },
	{ path: 'casopis/promeniOdbor/:id', component: IspraviOdborComponent },

	{ path: 'autor', component: AutorComponent, children: [
		{ path: '', redirectTo: 'radovi', pathMatch: 'full'},
		{ path: 'radovi', component: AutorRadComponent},
	]},
	

]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})

export class AppRoutingModule { }