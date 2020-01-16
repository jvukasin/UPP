import { Component, OnInit } from '@angular/core';
import { CasopisService } from 'src/app/services/casopis.service';

@Component({
  selector: 'app-dodaj-casopis',
  templateUrl: './dodaj-casopis.component.html',
  styleUrls: ['./dodaj-casopis.component.css']
})
export class DodajCasopisComponent implements OnInit {
  
  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues1 = [];
  private enumValues = [];
  private tasks = [];

  errNaucne: boolean = false;
  errNaziv: boolean = false;
  errIssn: boolean = false;
  errNaplata: boolean = false;

  constructor(private casopisService: CasopisService) {
    casopisService.startCasopisProcess().subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) => {
          
          if( field.type.name=='enum' && field.id=='nauc_oblasti'){
            this.enumValues1 = Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='naplata_clanarine') {
            this.enumValues = Object.keys(field.type.values);
          }
        });
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  ngOnInit() {
  }

  onSubmit(value, form){
    
    let o = new Array();
    for (var property in value) {
      console.log(property);
      console.log(value[property]);
      if(property === "nauc_oblasti" || property === "nacin_placanja") {
        var niz = value[property];
        for (let i=0; i<niz.length; i++) {
          o.push({fieldId : property, fieldValue : niz[i]});
        }
      } else {
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    console.log(o);
    var naziv = "";
    var issn = "";
    var countNO = 0;
    var naplata = "";
    for (let i=0; i<o.length; i++) {
      if(o[i].fieldId === "naziv") {
        naziv = o[i].fieldValue;
      } else if (o[i].fieldId === "issn") {
        issn = o[i].fieldValue;
      } else if (o[i].fieldId === "naplata_clanarine") {
        naplata = o[i].fieldValue;
      } else if (o[i].fieldId === "nauc_oblasti") {
        countNO = countNO + 1;
      }
    }
    if(this.checkEmpty(naziv, issn, naplata) && this.checkNaucne(countNO)) {
      let x = this.casopisService.postCasopis(o, this.formFieldsDto.taskId);
      x.subscribe(
        res => {
          console.log(res);
          window.location.href="http://localhost:4200/casopis/" + this.processInstance;
        },
        err => {
            alert("Error occured");
        }
      );
    }
  }

  checkEmpty(naziv, issn, naplata) : boolean {
    if(naziv === "") {
      this.errNaziv = true;
      return false;
    }
    this.errNaziv = false;
    if(issn === "") {
      this.errIssn = true;
      return false;
    }
    this.errIssn = false;
    if(naplata === "") {
      this.errNaplata = true;
      return false;
    }
    this.errNaplata = false;
    return true;
  }

  checkNaucne(count) {
    if(count < 1) {
      this.errNaucne = true;
      return false;
    }
    this.errNaucne = false;
    return true;
  }
}