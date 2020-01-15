import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegistrationComponent } from './registration/registration.component';
import { VerifyEmailComponent } from './registration/verify-email/verify-email.component';
import { VerificationDoneComponent } from './verification-done/verification-done.component';
import { AdminComponent } from './admin/admin.component';
import { PotvrdiRecenzentaComponent } from './admin/potvrdi-recenzenta/potvrdi-recenzenta.component';
import { LoginComponent } from './login/login.component';
import { CasopisComponent } from './casopis/urednik.component';
import { DodajOdborComponent } from './casopis/dodaj-odbor-form/dodaj-odbor.component';
import { DodajCasopisComponent } from './casopis/dodaj-casopis-form/dodaj-casopis.component';
import { AdminRecenzentiComponent } from './admin/admin-recenzenti/admin-recenzenti.component';
import { AdminUredniciComponent } from './admin/admin-urednici/admin-urednici.component';
import { UrednikCasopisComponent } from './casopis/urednik-casopis/urednik-casopis.component';


const appRoutes: Routes = [
	{ path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
	{ path: 'registration', component: RegistrationComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'verify', component: VerifyEmailComponent },
	{ path: 'verified/:pcs/:usr', component: VerificationDoneComponent },
	{ path: 'admin', component: AdminComponent, children: [
		{ path: '', redirectTo: 'recenzenti', pathMatch: 'full'},
		{ path: 'recenzenti', component: AdminRecenzentiComponent},
		{ path: 'urednici', component: AdminUredniciComponent},
	]},
	{ path: 'admin/:id', component: PotvrdiRecenzentaComponent },
	{ path: 'urednik', component: CasopisComponent, children: [
		{ path: '', redirectTo: 'casopisi', pathMatch: 'full'},
		{ path: 'casopisi', component: UrednikCasopisComponent},
	]},
	{ path: 'casopis/dodaj', component: DodajCasopisComponent},
	{ path: 'casopis/:id', component: DodajOdborComponent},

]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})

export class AppRoutingModule { }