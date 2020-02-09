import { Component, OnInit } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-urednik-rad',
  templateUrl: './urednik-rad.component.html',
  styleUrls: ['./urednik-rad.component.css']
})
export class UrednikRadComponent implements OnInit {

  lclhst: string = "http://localhost:4202";
  tasks = [];
  tasksRecen = [];

  constructor(private radService: RadService, private router: Router) {
    this.radService.getUrednikRadTasks().subscribe(
      res => {
        this.tasks = res;
      },
      err => {
        console.log("Error occured Rad tasks");
      }
    );

    this.radService.getUrednikIzborRecTasks().subscribe(
      res => {
        this.tasksRecen = res;
      },
      err => {
        console.log("Error occured while fetching IZ tasks");
      }
    );

  }

  ngOnInit() {
  }

  onClick(id) {
    this.router.navigate(['/obrada-rada/'.concat(id)]);
  }

  onClickRecen(id) {
    this.router.navigate(['/izbor-recenzenata/'.concat(id)]);
  }

}
