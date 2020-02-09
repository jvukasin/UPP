import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-dodaj-rad',
  templateUrl: './dodaj-rad.component.html',
  styleUrls: ['./dodaj-rad.component.css']
})
export class DodajRadComponent implements OnInit {

  pid: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private tasks = [];
  fileUrl: string;
  fileToUpload: File;
  lclhst: string = "http://localhost:4202";
  radId: any;

  constructor(private route: ActivatedRoute, private radService: RadService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.pid = params['id'];
      }
    );

    radService.getRadForm(this.pid).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) => {
          
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

  onSubmit(value, form){
    let o = new Array();
    for(var property in value){
      o.push({fieldId: property, fieldValue: value[property]});
    }
    if(this.proveri(o)) {
      this.radService.postRad(o, this.formFieldsDto.taskId).subscribe(
        res => {
          this.radId = res;
          const formData = new FormData();  
          formData.append("file", this.fileToUpload);
          this.radService.postFile(this.radId, formData).subscribe(
            data => {
              window.location.href= this.lclhst + "/autor";
            }, error => {
              alert("error uploading file");
            }
          );
        },
        err => {
            alert("Error occured");
        }
      );
    }
    
  }

  proveri(o) {
    for(var i = 0; i<o.length; i++) {
      if(o[i].fieldValue === "") {
        return false;
      } else if (o[i].fieldId === "br_koautora") {
        var broj = +o[i].fieldValue;
        if(broj <= 0) {
          return false;
        }
      }
    }
    return true;
  }

  onKeydown(e) {
    if(!((e.keyCode > 95 && e.keyCode < 106)
      || (e.keyCode > 47 && e.keyCode < 58) 
      || e.keyCode == 8 || e.keyCode == 37 || e.keyCode == 39 || e.keyCode == 9)) {
        return false;
    }
  }

}
