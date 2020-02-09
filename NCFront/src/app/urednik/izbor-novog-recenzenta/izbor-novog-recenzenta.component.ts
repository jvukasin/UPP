import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-izbor-novog-recenzenta',
  templateUrl: './izbor-novog-recenzenta.component.html',
  styleUrls: ['./izbor-novog-recenzenta.component.css']
})
export class IzborNovogRecenzentaComponent implements OnInit {

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

    this.radService.getNoviRecenzentForm(this.taskId).subscribe(
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
      o.push({fieldId : property, fieldValue : value[property]});
    }

    if(this.checkRec(o)) {
      this.spinner.show();
      this.radService.postNoviRecenzent(o, this.formFieldsDto.taskId).subscribe(
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

  checkRec(o) {
    if(o[0].fieldValue === "") {
      return false;
    }

    return true;
  }

}
