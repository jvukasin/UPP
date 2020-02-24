import { Component, OnInit } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-advanced',
  templateUrl: './advanced.component.html',
  styleUrls: ['./advanced.component.css']
})
export class AdvancedComponent implements OnInit {

  operation: any;
  sciencePaperList: any = [];
  searching: boolean = false;
  emptyList: boolean = false;

  private radioCheck="AND";
  private title="";
  private magazineName="";
  private keyTerms="";
  private author="";
  private scienceField="";
  private text="";

  constructor(private searchService: SearchService) { }

  ngOnInit() {
  }

  onSearch() {
    this.searching = true;

    let search = {
      magazineName: this.magazineName,
      title: this.title,
      keyTerms: this.keyTerms,
      text: this.text,
      author: this.author,
      scienceField: this.scienceField,
      checkAndOr: this.radioCheck
    }

    this.searchService.advancedSearch(search).subscribe(
      (response) => {
        this.searching = false;
        this.emptyList= false;
        this.sciencePaperList = response;
        if(this.sciencePaperList.length == 0){
          this.emptyList = true;
        }
      },
      (error) => { 
        alert(error.message);
        this.searching = false;
      }
    );
  }

}
