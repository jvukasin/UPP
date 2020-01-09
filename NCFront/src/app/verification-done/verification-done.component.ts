import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-verification-done',
  templateUrl: './verification-done.component.html',
  styleUrls: ['./verification-done.component.css']
})
export class VerificationDoneComponent implements OnInit {

  usr: String;
  pcs: String;

  constructor(private route: ActivatedRoute, private usrService: UserService) {
    // getting route params, params is observable that unsubscribes automatically
    this.route.params.subscribe(
      (params: Params) => {
        this.pcs = params['pcs'];
        this.usr = params['usr'];
      }
    );
  }

  ngOnInit() {
    this.usrService.verifyUser(this.pcs, this.usr).subscribe(
      res => {
        console.log(res);
        // window.location.href="http://localhost:4200/verify";
      },
      err => {
        console.log("Error occured");
      }
    );

  }

}
