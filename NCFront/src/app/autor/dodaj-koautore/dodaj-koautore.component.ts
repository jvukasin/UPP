import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-dodaj-koautore',
  templateUrl: './dodaj-koautore.component.html',
  styleUrls: ['./dodaj-koautore.component.css']
})
export class DodajKoautoreComponent implements OnInit {

  id: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private tasks = [];
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private radService: RadService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.id = params['id'];
      }
    );

    radService.getFormFromTask(this.id).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
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

  ngOnInit() {
  }

  onSubmit(value, form){
    let o = new Array();
    for(var property in value){
      o.push({fieldId: property, fieldValue: value[property]});
    }
    if(this.proveri(o)) {
      this.radService.postKoautor(o, this.formFieldsDto.taskId).subscribe(
        res => {
          window.location.href= this.lclhst + "/autor";
        },
        err => {
          alert("Error occured");
        }
      );
     
    }
  }

  proveri(o) {
    for(var i=1; i<o.length; i++) {
      if(o[i].fieldValue === "") {
        return false;
      }
    }
    return true;
  }

}
