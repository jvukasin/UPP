import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-recenziranje',
  templateUrl: './recenziranje.component.html',
  styleUrls: ['./recenziranje.component.css']
})
export class RecenziranjeComponent implements OnInit {

  taskId: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private tasks = [];
  private processInstance;
  
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private radService: RadService, private spinner: NgxSpinnerService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );

    radService.getFormFromTask(this.taskId).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.processInstance = res.processInstanceId;
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
      this.spinner.show();
      this.radService.postRecenzija(o, this.formFieldsDto.taskId).subscribe(
        res => {
          this.spinner.hide();
          window.location.href= this.lclhst;
        },
        err => {
          alert("Error occured");
          this.spinner.hide();
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

  onPreuzmiRad() {
    this.radService.downloadFile(this.processInstance).subscribe(
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
