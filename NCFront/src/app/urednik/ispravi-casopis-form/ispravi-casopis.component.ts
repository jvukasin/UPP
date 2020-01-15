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
  private tasks = [];
  private taskId;
  controls: any = [];

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
            this.enumValues = Object.keys(field.type.values);
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
      o.push({fieldId: control, fieldValue: this.controls[control].value});
    }

    console.log(o);
    let x = this.casopisService.posaljiIspravljeniCasopis(this.formFieldsDto.taskId, o);
    x.subscribe(
      res => {
        console.log(res);
        this.router.navigate(["/urednik"]);
      },
      err => {
        alert("Error occured");
      }
    );
  }

}
