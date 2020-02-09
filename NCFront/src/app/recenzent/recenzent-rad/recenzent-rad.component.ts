import { Component, OnInit } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-recenzent-rad',
  templateUrl: './recenzent-rad.component.html',
  styleUrls: ['./recenzent-rad.component.css']
})
export class RecenzentRadComponent implements OnInit {

  lclhst: string = "http://localhost:4202";
  tasks = [];

  constructor(private radService: RadService, private router: Router) {
    this.radService.getRecenzentRecenziranjeTasks().subscribe(
      res => {
        this.tasks = res;
      },
      err => {
        console.log("Error occured Rad tasks");
      }
    );

  }

  ngOnInit() {
  }

  onClick(id) {
    this.router.navigate(['/recenziranje-rada/'.concat(id)]);
  }

}
