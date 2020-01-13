import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import {map} from 'rxjs/operators';
import { Router } from '@angular/router';


@Injectable({
    providedIn: 'root'
})
export class AuthService {
    loggedUser: any;
	constructor(private http: HttpClient, private router: Router) { }

    login(user){
        return this.http.post('/api/auth/login', user)
        .pipe(
            map(user => {
                this.loggedUser = user;
                if(user){
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }
                return user;
            })
        )
    }

    logout(){
        return this.http.post('/api/auth/logout',null).subscribe(
            success => {
                localStorage.removeItem('currentUser');
                this.router.navigate(['']);
           }, error => alert('Error while trying to logout.')
        )
    }

    getLoggedUser() {
        return localStorage.getItem('currentUser');
    }

    searchDemo(param) {
        return this.http.post("/api/auth/search", param);
    }
 
}

