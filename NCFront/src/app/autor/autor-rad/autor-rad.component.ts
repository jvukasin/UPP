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
  tasksKom = [];
  radList: any = [];
  emptyRadList: boolean = false;
  retHref: any;
  imaTaskovaZaKoautore: boolean = false;
  imaTaskovaZaRadove: boolean = false;
  imaTaskovaSaKomentarima: boolean = false;

  lclhst: string = "http://localhost:4202";

  showModal: boolean = false;

  constructor(private radService: RadService, private router: Router) { }

  ngOnInit() {
    this.radService.getAutorRadTasks().subscribe(
      res => {
        this.tasksK = res;
        if(this.tasksK.length > 0) {
          this.imaTaskovaZaKoautore = true;
        }
      },
      err => {
        console.log("Error occured");
      }
    );

    this.radService.getAutorIspravkaRadaTasks().subscribe(
      res => {
        this.tasksR = res;
        if(this.tasksR.length > 0) {
          this.imaTaskovaZaRadove = true;
        }
      },
      err => {
        console.log("Error occured");
      }
    );

    this.radService.getAutorKomentariTasks().subscribe(
      res => {
        this.tasksKom = res;
        if(this.tasksKom.length > 0) {
          this.imaTaskovaSaKomentarima = true;
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

  onClickR(id) {
    this.router.navigate(['/ispravi/rad/'.concat(id)]);
  }

  onClickKom(id) {

  }

  onDodajRad() {
    this.showModal = true;
  }

  onCloseModal() {
    this.showModal = false;
  }


}
