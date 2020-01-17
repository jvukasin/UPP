import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { CasopisService } from 'src/app/services/casopis.service';

@Component({
  selector: 'app-ispravi-casopis',
  templateUrl: './ispravi-casopis.component.html',
  styleUrls: ['./ispravi-casopis.component.css']
})
export class IspraviCasopisComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues = [];
  private enumValues1 = [];
  private enumValues2 = [];
  private tasks = [];
  private taskId;
  controls: any = [];

  errNaziv: boolean = false;
  errIssn: boolean = false;
  errNaplata: boolean = false;

  constructor(private router: Router, private casopisService: CasopisService, private route: ActivatedRoute) {
    // getting route params, params is observable that unsubscribes automatically
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );

    this.casopisService.getIspravkaForm(this.taskId).subscribe(
      res => {
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum'){
            if(field.id == 'naplata_clanarineIzmena') {
              this.enumValues = Object.keys(field.type.values);
            } else if (field.id == 'naucneIzmena') {
              this.enumValues1 = Object.keys(field.type.values);
            } else if (field.id == 'izabrane_naucne') {
              this.enumValues2 = Object.keys(field.type.values);
            }
            
          }
        });
      },
      err => {
        alert("Error occured");
      } 
    );
  }

  ngOnInit() {
  }

  onSubmit(value, form){
    
    let o = new Array();
    this.controls = form.controls;
    for(var control in this.controls){
      if(control === "naucneIzmena") {
        var niz = this.controls[control].value;
        for (let i=0; i<niz.length; i++) {
          o.push({fieldId : control, fieldValue : niz[i]});
        }
      } else {
        o.push({fieldId: control, fieldValue: this.controls[control].value});
      }
    }

    console.log(o);
    var naziv = "";
    var issn = "";
    var naplata = "";
    for (let i=0; i<o.length; i++) {
      if(o[i].fieldId === "nazivIzmena") {
        naziv = o[i].fieldValue;
      } else if (o[i].fieldId === "issnIzmena") {
        issn = o[i].fieldValue;
      } else if (o[i].fieldId === "naplata_clanarineIzmena") {
        naplata = o[i].fieldValue;
      }
    }
    if(this.checkEmpty(naziv, issn, naplata)) {
      let x = this.casopisService.posaljiIspravljeniCasopis(this.formFieldsDto.taskId, o);
      x.subscribe(
        res => {
          console.log(res);
          window.location.href="http://localhost:4200/casopis/promeniOdbor/" + this.processInstance;
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
}
