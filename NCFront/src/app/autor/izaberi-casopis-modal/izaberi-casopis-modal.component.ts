import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-izaberi-casopis-modal',
  templateUrl: './izaberi-casopis-modal.component.html',
  styleUrls: ['./izaberi-casopis-modal.component.css']
})
export class IzaberiCasopisModalComponent implements OnInit {

  @Output() submit = new EventEmitter();

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues1 = [];
  private enumValues = [];
  private tasks = [];

  constructor(private radService: RadService) { }

  ngOnInit() {
    this.radService.startRadProcess().subscribe(
      res => {
        this.formFieldsDto = res;
      this.formFields = res.formFields;
      this.processInstance = res.processInstanceId;
      this.formFields.forEach( (field) => {
        
        if( field.type.name=='enum'){
          this.enumValues = Object.keys(field.type.values);
        }
      });
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
      console.log(property);
      console.log(value[property]);
      if(property === "nauc_oblasti") {
        var niz = value[property];
        for (let i=0; i<niz.length; i++) {
          o.push({fieldId : property, fieldValue : niz[i]});
        }
      } else {
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    
  }

}
