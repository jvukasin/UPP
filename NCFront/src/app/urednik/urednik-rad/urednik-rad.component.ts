import { Component, OnInit } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-urednik-rad',
  templateUrl: './urednik-rad.component.html',
  styleUrls: ['./urednik-rad.component.css']
})
export class UrednikRadComponent implements OnInit {

  lclhst: string = "http://localhost:4202";
  tasks = [];

  constructor(private radService: RadService) {
    let x = this.radService.getUrednikRadTasks();

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

  ngOnInit() {
  }

}
