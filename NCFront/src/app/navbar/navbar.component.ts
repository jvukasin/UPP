import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  isUrednik: boolean = false;
  isAutor: boolean = false;
  isRecenzent: boolean = false;
  isKorisnik: boolean = false;

  constructor(private authService: AuthService) {
    var rola = localStorage.getItem('rola');
    if(rola === "Admin") {
      this.isAdmin = true;
    } else if (rola === "Urednik") {
      this.isUrednik = true;
    } else if (rola === "Autor") {
      this.isAutor = true;
    } else if (rola === "Recenzent") {
      this.isRecenzent = true;
    } else if (rola === "Korisnik") {
      this.isKorisnik = true;
    }
    let user = this.authService.getLoggedUser();
    if(user != null){
      this.isLoggedIn = true; 
    } else {
      this.isLoggedIn = false;
    }
  }

  ngOnInit() {
  }

  logOut(){
    this.authService.logout();
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.isUrednik = false;
    this.isAutor = false;
    this.isRecenzent = false;
    this.isKorisnik = false;
    localStorage.setItem('rola', '');
    localStorage.setItem('user', '');
  }

}
