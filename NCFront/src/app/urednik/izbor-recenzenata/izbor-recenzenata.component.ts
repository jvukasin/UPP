import { Component, OnInit } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';
import { ActivatedRoute, Params } from '@angular/router';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-izbor-recenzenata',
  templateUrl: './izbor-recenzenata.component.html',
  styleUrls: ['./izbor-recenzenata.component.css']
})
export class IzborRecenzenataComponent implements OnInit {

  taskId: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private tasks = [];

  errRec: boolean = false;
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private radService: RadService, private spinner: NgxSpinnerService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );

    this.radService.getRecenzentiForm(this.taskId).subscribe(
      res => {
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if(field.type.name=='enum'){
            this.enumValues= Object.keys(field.type.values);
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
      if(property === "recenzent") {
        var niz = value[property];
        for (let i=0; i<niz.length; i++) {
          o.push({fieldId : property, fieldValue : niz[i]});
        }
      } else {
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    var countR = 0;
    for (let i=0; i<o.length; i++) {
      if(o[i].fieldId === "recenzent") {
        countR = countR + 1;
      }
    }
    if(this.checkRec(countR)) {
      this.spinner.show();
      this.radService.postRecenzenti(o, this.formFieldsDto.taskId).subscribe(
        res => {
          this.spinner.hide();
          window.location.href= this.lclhst;
        },
        err => {
            this.spinner.hide();
            alert("Error occured");
        }
      );
    }
  }

  checkRec(count) {
    if(count < 2) {
      this.errRec = true;
      return false;
    }
    this.errRec = false;
    return true;
  }

}
