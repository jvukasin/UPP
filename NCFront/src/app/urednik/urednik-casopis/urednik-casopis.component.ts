import { Component, OnInit } from '@angular/core';
import { CasopisService } from 'src/app/services/casopis.service';

@Component({
  selector: 'app-urednik-casopis',
  templateUrl: './urednik-casopis.component.html',
  styleUrls: ['./urednik-casopis.component.css']
})
export class UrednikCasopisComponent implements OnInit {

  private tasks = [];

  constructor(private casopisService: CasopisService) { }

  ngOnInit() {
    let x = this.casopisService.getUrednikCasopisTasks();

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
