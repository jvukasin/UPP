import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-final-ispravka-rad',
  templateUrl: './final-ispravka-rad.component.html',
  styleUrls: ['./final-ispravka-rad.component.css']
})
export class FinalIspravkaRadComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues = [];
  private tasks = [];
  private taskId;

  controls: any = [];
  fileUrl: string;
  fileToUpload: File;
  lclhst: string = "http://localhost:4202";
  radId: any;
  fileName: any;

  err: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, private radService: RadService, private spinner: NgxSpinnerService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );

    this.radService.getFormFromTask(this.taskId).subscribe(
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
      if(control !== "naslov_kraj") {
        o.push({fieldId: control, fieldValue: this.controls[control].value});
      }
    }

    console.log(o);
    var fff = o[0].fieldValue;
    if(this.checkEmpty(fff)) {
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

  checkEmpty(fajl) : boolean {
    if(fajl === "") {
      this.err = true;
      return false;
    }
    this.err = false;
    return true;
  }

}
