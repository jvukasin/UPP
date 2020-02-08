import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository.service';
import { UserService } from '../services/user.service';
import { NgxSpinnerService } from "ngx-spinner";

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

  errFirst: boolean = false;
  errLast: boolean = false;
  errUsr: boolean = false;
  errMail: boolean = false;
  errPass: boolean = false;
  errNaucne: boolean = false;

  lclhst: string = "http://localhost:4202";

  constructor(private userService : UserService, private repositoryService : RepositoryService, private spinner: NgxSpinnerService) {

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

  onSubmit(value, form) {
    
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

    console.log(o);
    var ime = "";
    var prezime = "";
    var email = "";
    var username = "";
    var password = "";
    var countNO = 0;
    for (let i=0; i<o.length; i++) {
      if(o[i].fieldId === "ime") {
        ime = o[i].fieldValue;
      } else if (o[i].fieldId === "prezime") {
        prezime = o[i].fieldValue;
      } else if (o[i].fieldId === "email") {
        email = o[i].fieldValue;
      } else if (o[i].fieldId === "username") {
        username = o[i].fieldValue;
      } else if (o[i].fieldId === "password") {
        password = o[i].fieldValue;
      } else if (o[i].fieldId === "nauc_oblasti") {
        countNO = countNO + 1;
      }
    }
    if(this.checkNames(ime, prezime) && this.checkUsername(username) && this.checkMail(email) && this.checkPass(password) && this.checkNaucne(countNO)) {
      let x = this.userService.registerUser(o, this.formFieldsDto.taskId);
      this.spinner.show();
      x.subscribe(
        res => {
          console.log(res);
          window.location.href= this.lclhst + "/verify";
        },
        err => {
          alert(err.message);
          this.spinner.hide();
        }
      );
    }
  }

  /* VALIDATORS */

  checkNames(first, last) : boolean {
    const patt = /^[a-zA-Z]+$/;
    if(!patt.test(first)) {
      this.errFirst = true;
      return false;
    }
    this.errFirst = false;
    if(!patt.test(last)) {
      this.errLast = true;
      return false;
    }
    this.errLast = false;
    return true;
  }

  checkNaucne(count) {
    if(count < 1) {
      this.errNaucne = true;
      return false;
    }
    this.errNaucne = false;
    return true;
  }

  checkUsername(text) : boolean {
    if(text.includes('<') || text.includes(' ') || text.includes('>') || text.includes(';')) {
      this.errUsr = true;
      return false;
    }
    this.errUsr = false;
    return true;
  }

  checkPass(text) : boolean {
    const mailPatter = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
    if(!mailPatter.test(text)) {
      this.errPass = true;
      return false;
    }
    this.errPass = false;
    return true;
  }

  checkMail(text) : boolean {
    const mailPatter = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
    if(!mailPatter.test(text)) {
      this.errMail = true;
      return false;
    }
    this.errMail = false;
    return true;
  }
}
