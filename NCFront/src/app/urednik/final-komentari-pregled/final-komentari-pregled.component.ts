import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-final-komentari-pregled',
  templateUrl: './final-komentari-pregled.component.html',
  styleUrls: ['./final-komentari-pregled.component.css']
})
export class FinalKomentariPregledComponent implements OnInit {

  taskId: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private enumValues1 = [];

  errRec: boolean = false;
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private radService: RadService, private spinner: NgxSpinnerService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );

    this.radService.getFinalUrednikForm(this.taskId).subscribe(
      res => {
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if(field.type.name=='enum' && field.id=='kom_rec'){
            this.enumValues1= Object.keys(field.type.values);
          } else if (field.type.name=='enum' && field.id=='sta_s_radom') {
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
      if(property === "sta_s_radom") {
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    if(this.checkRec(o)) {
      this.spinner.show();
      this.radService.postFinalUrednik(o, this.formFieldsDto.taskId).subscribe(
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
