import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-pregled-recenzija',
  templateUrl: './pregled-recenzija.component.html',
  styleUrls: ['./pregled-recenzija.component.css']
})
export class PregledRecenzijaComponent implements OnInit {

  taskId: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private enumValues1 = [];
  private enumValues2 = [];
  private enumValues3 = [];

  errRec: boolean = false;
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private radService: RadService, private spinner: NgxSpinnerService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );

    this.radService.getPregledForm(this.taskId).subscribe(
      res => {
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if(field.type.name=='enum' && field.id=='posl_komentar_o_radu'){
            this.enumValues1= Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='posl_preporuka') {
            this.enumValues2= Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='posl_komentar_za_urednika') {
            this.enumValues3= Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='prihvati_rad') {
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
      if(property === "prihvati_rad") {
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    if(this.checkRec(o)) {
      this.spinner.show();
      this.radService.postOdlukaUrednika(o, this.formFieldsDto.taskId).subscribe(
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
      this.errRec = true;
      return false;
    }
    this.errRec = false;
    return true;
  }


}
