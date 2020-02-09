import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-obrada-rada',
  templateUrl: './obrada-rada.component.html',
  styleUrls: ['./obrada-rada.component.css']
})
export class ObradaRadaComponent implements OnInit {

  id: any;
  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues = [];
  private tasks = [];
  private taskId;
  controls: any = [];

  stajeovo: any;

  constructor(private route: ActivatedRoute, private radService: RadService, private router: Router, private spinner: NgxSpinnerService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.id = params['id'];
      }
    );

    this.radService.getFormFromTask(this.id).subscribe(
      res => {
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        });
      },
      err => {
        alert("Error getting form");
      } 
    );

  }

  ngOnInit() {
  }

  onSubmit(value, form){
    
    let o = new Array();
    this.controls = form.controls;
    for(var control in this.controls){
      if(control === "relevantan_rad" || control === "dobro_formiran" || control === "komentar_ur") {
        o.push({fieldId: control, fieldValue: this.controls[control].value});
      }
    }

    console.log(o);
    let x = this.radService.UrednikObradaRada(o, this.formFieldsDto.taskId);
    this.spinner.show();
    x.subscribe(
      res => {
        this.spinner.hide();
        this.router.navigate(["/pocetna"]);
      },
      err => {
        this.spinner.hide();
        alert("Error occured");
      }
    );
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
