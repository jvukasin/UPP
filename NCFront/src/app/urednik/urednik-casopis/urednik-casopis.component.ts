import { Component, OnInit } from '@angular/core';
import { CasopisService } from 'src/app/services/casopis.service';
import { KPService } from 'src/app/services/kp.service';

@Component({
  selector: 'app-urednik-casopis',
  templateUrl: './urednik-casopis.component.html',
  styleUrls: ['./urednik-casopis.component.css']
})
export class UrednikCasopisComponent implements OnInit {

  tasks = [];
  magazineList: any = [];
  emptyMagazineList: boolean = false;
  retHref: any;

  lclhst: string = "http://localhost:4202";

  constructor(private kpService: KPService, private casopisService: CasopisService) { }

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

    this.casopisService.getAllByUrednik().subscribe(
      (data) => {
        this.magazineList = data;
      }, 
      (error) => {
        alert(error.message);
      }
    )
  }

  onClick(id) {
    window.location.href = this.lclhst + "/casopis/promeni/" + id;
  }

  onRegisterMagazine(id) {

    let dto = {
      id: id
    }

    this.kpService.registerMagazinSeller(dto).subscribe(
      (res: any) => {
        this.magazineList = this.magazineList.map(m => {
          if (m.id == dto.id) {
            m.sellerId = "x";
          }

          return m;
        })
        window.location.href = res.registrationPageRedirectUrl;
      }, err=> console.log(err.error)
    )
  }

  onReviewRegistration(id) {
    
    let dto = {
      id: id
    }

    this.kpService.reviewRegistration(dto).subscribe(
      (res: any) => {
        window.location.href = res.registrationPageRedirectUrl;
      }, err => console.log(err.error)
    )
    
  }

  onPlan(magazineID) {
    this.kpService.createPlan(magazineID).subscribe(
      (response) => {
        this.retHref = response;
        if(this.retHref.href === "noPP") {
          alert("Nije moguce napraviti plan jer PayPal nije registrovan!");
        } else {
          window.location.href = this.retHref.href;
        }
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  onListPlans(magazineID) {
    this.kpService.getPlans(magazineID).subscribe(
      (data) => {
        this.retHref = data;
        window.location.href = this.retHref;
      }, (error) => {
        alert(error.message);
      }
    );
  }

}
