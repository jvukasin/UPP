import { Component, OnInit } from '@angular/core';
import { CasopisService } from '../services/casopis.service';

@Component({
  selector: 'app-casopis',
  templateUrl: './casopis.component.html',
  styleUrls: ['./casopis.component.css']
})
export class CasopisComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues1 = [];
  private enumValues2 = [];
  private tasks = [];

  constructor(private casopisService: CasopisService) {
    casopisService.startCasopisProcess().subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum' && field.id=='nauc_oblasti'){
            this.enumValues1 = Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='nacin_placanja') {
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
    let x = this.casopisService.postCasopis(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log(res);
        window.location.href="http://localhost:4200/home";
      },
      err => {
          alert("Error occured");
      }
    );
  }
}
