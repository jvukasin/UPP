import { Component, OnInit } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-autor-rad',
  templateUrl: './autor-rad.component.html',
  styleUrls: ['./autor-rad.component.css']
})
export class AutorRadComponent implements OnInit {

  tasksK = [];
  tasksR = [];
  radList: any = [];
  emptyRadList: boolean = false;
  retHref: any;
  imaTaskovaZaKoautore: boolean = false;
  imaTaskovaZaRadove: boolean = false;

  lclhst: string = "http://localhost:4202";

  showModal: boolean = false;

  constructor(private radService: RadService, private router: Router) { }

  ngOnInit() {
    let x = this.radService.getAutorRadTasks();

    x.subscribe(
      res => {
        console.log(res);
        this.tasksK = res;
        if(this.tasksK.length > 0) {
          this.imaTaskovaZaKoautore = true;
        }
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onClickK(id) {
    this.router.navigate(['/dodaj/rad/koautori/'.concat(id)]);
  }

  onDodajRad() {
    this.showModal = true;
  }

  onCloseModal() {
    this.showModal = false;
  }


}
