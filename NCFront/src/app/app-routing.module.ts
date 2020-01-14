import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegistrationComponent } from './registration/registration.component';
import { VerifyEmailComponent } from './registration/verify-email/verify-email.component';
import { VerificationDoneComponent } from './verification-done/verification-done.component';
import { AdminComponent } from './admin/admin.component';
import { PotvrdiRecenzentaComponent } from './admin/potvrdi-recenzenta/potvrdi-recenzenta.component';
import { LoginComponent } from './login/login.component';
import { CasopisComponent } from './casopis/casopis.component';
import { DodajOdborComponent } from './casopis/dodaj-odbor/dodaj-odbor.component';


const appRoutes: Routes = [
	{ path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
	{ path: 'registration', component: RegistrationComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'verify', component: VerifyEmailComponent },
	{ path: 'verified/:pcs/:usr', component: VerificationDoneComponent },
	{ path: 'admin', component: AdminComponent },
	{ path: 'admin/:id', component: PotvrdiRecenzentaComponent },
	{ path: 'casopis', component: CasopisComponent},
	{ path: 'casopis/:id', component: DodajOdborComponent},

]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})

export class AppRoutingModule { }