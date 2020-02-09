import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RadService } from 'src/app/services/rad.service';
import { MockService } from 'src/app/services/mock.service';

@Component({
  selector: 'app-uplata-clanarine',
  templateUrl: './uplata-clanarine.component.html',
  styleUrls: ['./uplata-clanarine.component.css']
})
export class UplataClanarineComponent implements OnInit {

  pid: any;
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private tasks = [];
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private mockService: MockService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.pid = params['id'];
      }
    );
    mockService.getForm(this.pid).subscribe(
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

  onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
      console.log(property);
      console.log(value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    }
    if(this.proveri(o[0])) {
      this.mockService.uplatiClanarinu(o, this.formFieldsDto.taskId).subscribe(
        res => {
          window.location.href= this.lclhst + "/dodaj/rad/" + this.pid;
        }, error => {
          alert("error uplacivanje");
        }
      );
    }
  }

  proveri(polje) {
    var broj = +polje.fieldValue;
    if(polje.fieldValue == "" || broj <= 0) {
      return false;
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
