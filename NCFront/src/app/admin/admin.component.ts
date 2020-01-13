import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  private repeated_password = "";
  private categories = [];
  private formFieldsDto = null;
  private formFields = [];
  private choosen_category = -1;
  private processInstance = "";
  private enumValues = [];
  private tasks = [];
  isLoggedIn: any;

  constructor(private repositoryService: RepositoryService, private authService: AuthService) { }

  ngOnInit() {
    let user = this.authService.getLoggedUser();
    if(user != null){
      this.isLoggedIn = true; 
    }

    let x = this.repositoryService.getAdminTasks();
  
    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onClick(id) {
    window.location.href = "http://localhost:4200/admin/" + id;
  }

  logOut(){
    this.authService.logout();
    this.isLoggedIn = false;
  }

}
