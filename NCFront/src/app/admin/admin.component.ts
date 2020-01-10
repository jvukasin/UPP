import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository.service';

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

  constructor(private repositoryService: RepositoryService) { }

  ngOnInit() {
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

}
