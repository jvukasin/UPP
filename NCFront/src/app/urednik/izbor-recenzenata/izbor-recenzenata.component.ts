import { Component, OnInit } from '@angular/core';
import { RadService } from 'src/app/services/rad.service';
import { ActivatedRoute, Params } from '@angular/router';
import { NgxSpinnerService } from "ngx-spinner";
import { SearchService } from 'src/app/services/search.service';
import { FinalKomentariPregledComponent } from '../final-komentari-pregled/final-komentari-pregled.component';

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

  pribavljeniRecPoNo: any;
  izabranElastic: boolean = false;

  errRec: boolean = false;
  lclhst: string = "http://localhost:4202";

  constructor(private route: ActivatedRoute, private radService: RadService, private spinner: NgxSpinnerService, private searchService: SearchService) {
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
            this.pribavljeniRecPoNo = this.enumValues;
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
      }
      if(property === 'filterOption'){
        continue;
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

  onChange(value){
    if(value == "Recenzenti sa istom naučnom oblašću kao i rad") {
      this.izabranElastic = false;
      this.enumValues = this.pribavljeniRecPoNo;
    } else if(value == "Recenzenti koji su recenzirali slične radove") {
      this.searchService.moreLikeThis(this.taskId).subscribe(
        (response: any) => {
          this.izabranElastic = true;
          var res = response;
          var rezultat = {};
          res.forEach(item => rezultat[item.username] = item.name);
          this.enumValues = Object.keys(rezultat);
        },
        (error) => { alert(error.message) }
      );
      
    }else if(value == "Recenzenti koji su udaljeni 100km od autora(i koautora)"){
      this.searchService.geoDistance(this.taskId).subscribe(
        (response: any) => {
          this.izabranElastic = true;
          var res = response;
          var rezultat = {};
          res.forEach(item => rezultat[item.username] = item.name);
          this.enumValues = Object.keys(rezultat);
        }
      ),
      (error) => { alert(error.message) }
    }
  }

}
