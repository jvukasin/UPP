import { Component, OnInit } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-autor-rad',
  templateUrl: './autor-rad.component.html',
  styleUrls: ['./autor-rad.component.css']
})
export class AutorRadComponent implements OnInit {

  tasks = [];
  radList: any = [];
  emptyRadList: boolean = false;
  retHref: any;

  lclhst: string = "http://localhost:4202";

  showModal: boolean = false;

  constructor(private radService: RadService) { }

  ngOnInit() {
    let x = this.radService.getAutorRadTasks();

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
    // window.location.href = this.lclhst + "/casopis/promeni/" + id;
    alert("kliknuo na dugme u zadacima");
  }

  onDodajRad() {
    this.showModal = true;
  }

  onCloseModal() {
    this.showModal = false;
  }

  casopisSubmitted(magID) {
    this.showModal = false;
    alert("Izabrani casopis je ".concat(magID));
    //KOD ZA PROCES
  }


}
