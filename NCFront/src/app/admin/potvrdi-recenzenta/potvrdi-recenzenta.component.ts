import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';

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

  constructor(private repositoryService : RepositoryService) {
    this.repositoryService.getAdminForm().subscribe(
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
    for (var property in value) {
      console.log(property);
      console.log(value[property]);
        o.push({fieldId : property, fieldValue : value[property]});
    }

    console.log(o);
    let x = this.repositoryService.potvrdaRecenzenta(this.formFieldsDto.taskId, o);
    x.subscribe(
      res => {
        console.log(res);
      },
      err => {
        alert("Error occured");
      }
    );
  }

}
