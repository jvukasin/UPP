import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-potvrdi-recenzenta',
  templateUrl: './potvrdi-recenzenta.component.html',
  styleUrls: ['./potvrdi-recenzenta.component.css']
})
export class PotvrdiRecenzentaComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private choosen_category = -1;
  private processInstance = "";
  private enumValues = [];
  private tasks = [];
  private taskId;
  controls: any = [];

  constructor(private repositoryService : RepositoryService, private route: ActivatedRoute, private router: Router) {
    // getting route params, params is observable that unsubscribes automatically
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );
    this.repositoryService.getAdminForm(this.taskId).subscribe(
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
    let x = this.repositoryService.potvrdaRecenzenta(this.formFieldsDto.taskId, o);
    x.subscribe(
      res => {
        console.log(res);
        this.router.navigate(["/admin"]);
      },
      err => {
        alert("Error occured");
      }
    );
  }

}
