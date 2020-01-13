import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    username: new FormControl(""),
    password: new FormControl("")
  })

  constructor(private authService: AuthService, private router: Router, private userService: UserService) { }

  ngOnInit() {
  }

  onSubmit(){

  let userDTO = {
    username: this.loginForm.value.username,
    password: this.loginForm.value.password
  }

  this.authService.login(userDTO).subscribe(
    (success) => {
      alert("succes");
      this.userService.getUser().subscribe(
        (user: any) => {
          if(user.role == "ADMIN"){
            this.router.navigate(["/admin"]);
          }
        }
      )
      this.router.navigate(["/homepage"]);
    }, 
    (error) => {
      alert(error);
    }
  )
  }
}
