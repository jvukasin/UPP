import { Component, OnInit } from '@angular/core';
import { CasopisService } from 'src/app/services/casopis.service';
import { ActivatedRoute, Params } from '@angular/router';
import { KPService } from 'src/app/services/kp.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-casopis-info',
  templateUrl: './casopis-info.component.html',
  styleUrls: ['./casopis-info.component.css']
})
export class CasopisInfoComponent implements OnInit {

  magazineId: any;
  magazine: any;
  retHref: any;

  constructor(private casopisService: CasopisService, private route: ActivatedRoute, private kpService: KPService, private orderService: OrderService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.magazineId = params['id'];
      }
    );


   }

  ngOnInit() {
    this.casopisService.get(this.magazineId).subscribe(
      (response) => {
        this.magazine = response;
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  onKupi(){
    this.orderService.initMagazineOrder(this.magazine).subscribe(
      (response: any) => {
        window.location.href = response.redirectUrl;
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  onPretplatise() {
    this.orderService.initMagazineSubscription(this.magazine).subscribe(
      (response: any) => {
        window.location.href = response.redirectUrl;
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  onKupiRad(paper) {
    this.orderService.initPaperOrder(paper).subscribe(
      (response: any) => {
        window.location.href = response.redirectUrl;
      },
      (error) => {
        alert(error.message);
      }
    )
  }
}
