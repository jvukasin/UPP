import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';

@Component({
  selector: 'app-admin-urednici',
  templateUrl: './admin-urednici.component.html',
  styleUrls: ['./admin-urednici.component.css']
})
export class AdminUredniciComponent implements OnInit {

  private tasks = [];

  constructor(private repositoryService: RepositoryService) { }

  ngOnInit() {
    let x = this.repositoryService.getAdminUrdTasks();
  
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
    window.location.href = "http://localhost:4200/admin/casopis/" + id;
  }

}
