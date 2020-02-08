import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { CasopisService } from 'src/app/services/casopis.service';

@Component({
  selector: 'app-ispravi-odbor',
  templateUrl: './ispravi-odbor.component.html',
  styleUrls: ['./ispravi-odbor.component.css']
})
export class IspraviOdborComponent implements OnInit {

  pid: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues1 = [];
  private enumValues2 = [];
  private tasks = [];
  controls: any = [];

  errRec: boolean = false;
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private casopisService: CasopisService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.pid = params['id'];
      }
    );

    this.casopisService.getOdborForma(this.pid).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum' && field.id=='uredniciIzmena'){
            this.enumValues1 = Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='recenzentiIzmena') {
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
    this.controls = form.controls;
    for (var control in this.controls) {
      if(control === "uredniciIzmena" || control === "recenzentiIzmena") {
        var niz = this.controls[control].value;
        for (let i=0; i<niz.length; i++) {
          o.push({fieldId : control, fieldValue : niz[i]});
        }
      } else {
        o.push({fieldId: control, fieldValue: this.controls[control].value});
      }
    }

    console.log(o);
    var countR = 0;
    for (let i=0; i<o.length; i++) {
      if(o[i].fieldId === "recenzentiIzmena") {
        countR = countR + 1;
      }
    }
    if(this.checkRec(countR)) {
      let x = this.casopisService.postIzmenaOdbor(o, this.formFieldsDto.taskId);
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
