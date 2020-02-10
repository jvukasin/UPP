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
  tasksAgain = [];
  tasksRecenziraj = [];
  tasksPregled = [];
  tasksFinal = [];

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

    this.radService.getUrednikIzborRecOpetTasks().subscribe(
      res => {
        this.tasksAgain = res;
      },
      err => {
        console.log("Error occured Rad tasks");
      }
    );

    this.radService.getRecenzentRecenziranjeTasks().subscribe(
      res => {
        this.tasksRecenziraj = res;
        console.log(this.tasksRecenziraj);
      },
      err => {
        console.log("Error occured Rad tasks");
      }
    );

    this.radService.getUrednikPregledaTasks().subscribe(
      res => {
        this.tasksPregled = res;
      },
      err => {
        console.log("Error occured Rad tasks");
      }
    );

    this.radService.getUrednikFinalTasks().subscribe(
      res => {
        this.tasksFinal = res;
      },
      err => {
        console.log("Error occured Rad tasks");
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

  onClickAgain(id) {
    this.router.navigate(['/izbor-novog-recenzenta/'.concat(id)]);
  }

  onClickRecenziraj(id) {
    this.router.navigate(['/recenziranje-rada/'.concat(id)]);
  }

  onClickPregled(id) {
    this.router.navigate(['/pregled/recenzije/'.concat(id)]);
  }

  onClickFinal(id) {
    this.router.navigate(['/final/pregled/'.concat(id)]);
  }

}
