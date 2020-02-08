import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CasopisService } from 'src/app/services/casopis.service';

@Component({
  selector: 'app-casopis-list',
  templateUrl: './casopis-list.component.html',
  styleUrls: ['./casopis-list.component.css']
})
export class CasopisListComponent implements OnInit {

  magazines: any[] = null;
  
	constructor(private router: Router, private casopisService: CasopisService) {

		this.casopisService.getAll().subscribe(
      (data: any) => {
        this.magazines = data;
      },
      (error) => {
        alert(error.message);
      }
    );
	}

	ngOnInit() {}

	showMagazine(id){
		this.router.navigate(['/pocetna/casopisi/' + id]);
	}

}
