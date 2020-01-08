import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  private repeated_password = "";
  private categories = [];
  private formFieldsDto = null;
  private formFields = [];
  private choosen_category = -1;
  private processInstance = "";
  private enumValues = [];
  private tasks = [];
  showNaucneOblasti: boolean = false;

  constructor(private userService : UserService, private repositoryService : RepositoryService) {

    repositoryService.startProcess().subscribe(
      res => {
        console.log(res);
        //this.categories = res;
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
      if(property == "naucna_oblast") {
        o.push({fieldId : property, fieldValue : value[property]}); //OVDE IZMENI DA IZ LISTE CUPA PO JEDNOG
      } else {
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    console.log(o);
    let x = this.userService.registerUser(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        console.log(res);
        
        alert("Verify your email address!")
      },
      err => {
        console.log("Error occured");
      }
    );
  }

}
