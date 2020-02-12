import { Component, OnInit } from '@angular/core';
import { CasopisService } from 'src/app/services/casopis.service';
import { ActivatedRoute, Params } from '@angular/router';
import { KPService } from 'src/app/services/kp.service';
import { OrderService } from 'src/app/services/order.service';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-casopis-info',
  templateUrl: './casopis-info.component.html',
  styleUrls: ['./casopis-info.component.css']
})
export class CasopisInfoComponent implements OnInit {

  magazineId: any;
  magazine: any;
  retHref: any;
  pretplacen: boolean = false;

  ulogovanKorisnik: boolean = false;

  constructor(private casopisService: CasopisService, private radService: RadService, private route: ActivatedRoute, private kpService: KPService, private orderService: OrderService) {
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
        var casopis = this.magazine;
        var rola = localStorage.getItem('rola');
        if(rola === "Korisnik") {
          this.ulogovanKorisnik = true;
          this.daLiJePretplacen(casopis.id);
        }
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  daLiJePretplacen(casID) {
    this.casopisService.proveriPretplacen(casID).subscribe(
      (res) => {
        var jelJe = res;
        if(jelJe === "Subbed") {
          this.pretplacen = true;
        }
      }, err => {
        alert("error pretplacen");
      }
    );
  }

  onKupi(){
    var casopis = {
        id: this.magazine.id,
        name: this.magazine.name,
        issn: this.magazine.issn,
        isRegistered: true,
        sellerId: this.magazine.sellerId,
        scienceFieldList: this.magazine.scienceFieldList,
        chiefEditor: this.magazine.chiefEditor,
        sciencePaperDTOList: this.magazine.sciencePaperDTOList,
        active: this.magazine.active
    }
    this.orderService.initMagazineOrder(casopis).subscribe(
      (response: any) => {
        window.location.href = response.redirectUrl;
      },
      (error) => {
        alert(error.message);
      }
    );
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
    );
  }

  onPreuzmiRad(paper) {
    this.radService.downloadFileByradID(paper.id).subscribe(
      res => {
        var blob = new Blob([res], {type: 'application/pdf'});
        var url= window.URL.createObjectURL(blob);
        window.open(url, "_blank");
      }, err => {
        alert("Error while download file");
      }
    );
  }
}
