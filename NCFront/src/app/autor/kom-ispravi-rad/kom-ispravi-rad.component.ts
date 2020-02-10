import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-kom-ispravi-rad',
  templateUrl: './kom-ispravi-rad.component.html',
  styleUrls: ['./kom-ispravi-rad.component.css']
})
export class KomIspraviRadComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues = [];
  private enumValues1 = [];
  private enumValues2 = [];
  private tasks = [];
  private taskId;

  controls: any = [];
  fileUrl: string;
  fileToUpload: File;
  lclhst: string = "http://localhost:4202";
  radId: any;
  fileName: any;

  errNaslov: boolean = false;
  errPojmovi: boolean = false;
  errAbstrakt: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, private radService: RadService, private spinner: NgxSpinnerService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );

    this.radService.getKomentarIspravkaForm(this.taskId).subscribe(
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
      if(control !== "prikaz_recen_komentara") {
        o.push({fieldId: control, fieldValue: this.controls[control].value});
      }
    }

    console.log(o);
    var naslov = "";
    var pojmovi = "";
    var apstrakt = "";
    for (let i=0; i<o.length; i++) {
      if(o[i].fieldId === "naslov3") {
        naslov = o[i].fieldValue;
      } else if (o[i].fieldId === "kljucni_pojmovi3") {
        pojmovi = o[i].fieldValue;
      } else if (o[i].fieldId === "apstrakt3") {
        apstrakt = o[i].fieldValue;
      }
    }
    if(this.checkEmpty(naslov, pojmovi, apstrakt)) {
      this.spinner.show();
      this.radService.postIspravkeRada(o, this.formFieldsDto.taskId).subscribe(
        res => {
          this.radId = res;
          const formData = new FormData();  
          formData.append("file", this.fileToUpload);
          this.radService.postFile(this.radId, formData).subscribe(
            data => {
              window.location.href= this.lclhst + "/autor";
            }, error => {
              this.spinner.hide();
              alert("error uploading file");
            }
          );
        },
        err => {
          this.spinner.hide();
          alert("Error occured");
        }
      );
    }
  }

  handleFileInput(file:FileList){
    this.fileToUpload =file.item(0);
    var reader = new FileReader();
    reader.onload=(event:any)=>{
      this.fileUrl =event.target.result;
    }
    reader.readAsDataURL(this.fileToUpload);
    console.log("URL "+this.fileUrl);
    console.log("file "+this.fileToUpload);
  }

  checkEmpty(naziv, issn, naplata) : boolean {
    if(naziv === "") {
      this.errNaslov = true;
      return false;
    }
    this.errNaslov = false;
    if(issn === "") {
      this.errPojmovi = true;
      return false;
    }
    this.errPojmovi = false;
    if(naplata === "") {
      this.errAbstrakt = true;
      return false;
    }
    this.errAbstrakt = false;
    return true;
  }

}
