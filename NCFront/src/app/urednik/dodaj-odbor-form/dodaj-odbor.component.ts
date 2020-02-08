import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { CasopisService } from 'src/app/services/casopis.service';

@Component({
  selector: 'app-dodaj-odbor',
  templateUrl: './dodaj-odbor.component.html',
  styleUrls: ['./dodaj-odbor.component.css']
})
export class DodajOdborComponent implements OnInit {
  
  pid: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues1 = [];
  private enumValues2 = [];
  private tasks = [];

  errRec: boolean = false;
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private casopisService: CasopisService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.pid = params['id'];
      }
    );

    casopisService.getOdborForma(this.pid).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum' && field.id=='urednici'){
            this.enumValues1 = Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='recenzenti') {
            this.enumValues2 = Object.keys(field.type.values);
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
      if(property === "urednici" || property === "recenzenti") {
        var niz = value[property];
        for (let i=0; i<niz.length; i++) {
          o.push({fieldId : property, fieldValue : niz[i]});
        }
      } else {
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    console.log(o);
    var countR = 0;
    for (let i=0; i<o.length; i++) {
      if(o[i].fieldId === "recenzenti") {
        countR = countR + 1;
      }
    }
    if(this.checkRec(countR)) {
      let x = this.casopisService.postOdbor(o, this.formFieldsDto.taskId);
      x.subscribe(
        res => {
          console.log(res);
          window.location.href= this.lclhst + "/urednik";
        },
        err => {
            alert("Error occured");
        }
      );
    }
  }

  checkRec(count) {
    if(count < 2) {
      this.errRec = true;
      return false;
    }
    this.errRec = false;
    return true;
  }

}
