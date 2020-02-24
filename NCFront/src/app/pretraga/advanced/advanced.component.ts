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

  private magazineCheck=true;
  private titleCheck=true;
  private contentCheck=true;
  private keyTermsCheck=true;
  private authorsCheck=true;
  private NOCheck=true;

  constructor(private searchService: SearchService) { }

  ngOnInit() {
  }

}
