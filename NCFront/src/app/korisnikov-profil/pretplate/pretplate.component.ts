import { Component, OnInit } from '@angular/core';
import { KPService } from 'src/app/services/kp.service';

@Component({
  selector: 'app-pretplate',
  templateUrl: './pretplate.component.html',
  styleUrls: ['./pretplate.component.css']
})
export class PretplateComponent implements OnInit {

  agrList: any;
  emptyList: boolean = true;

  constructor(private kpServis: KPService) {
    this.kpServis.getUserAgreements().subscibe(
      res => {
        this.agrList = res;
        if(this.agrList.lenth != 0) {
          this.emptyList = false;
        }
      }, err => {
        alert("error getting subscriptions");
      }
    );
  }

  ngOnInit() {
  }

}
