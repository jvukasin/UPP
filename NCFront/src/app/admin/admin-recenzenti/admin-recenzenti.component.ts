import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';

@Component({
  selector: 'app-admin-recenzenti',
  templateUrl: './admin-recenzenti.component.html',
  styleUrls: ['./admin-recenzenti.component.css']
})
export class AdminRecenzentiComponent implements OnInit {

  private tasks = [];
  lclhst: string = "http://localhost:4202";

  constructor(private repositoryService: RepositoryService) { }

  ngOnInit() {
    let x = this.repositoryService.getAdminRecTasks();
  
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
    window.location.href = this.lclhst.concat("/admin/").concat(id);
  }

}
