import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order.service';
import { RadService } from 'src/app/services/rad.service';

@Component({
  selector: 'app-porudzbine',
  templateUrl: './porudzbine.component.html',
  styleUrls: ['./porudzbine.component.css']
})
export class PorudzbineComponent implements OnInit {

  emptyList: boolean = true;
  orderList: any;

  constructor(private orderService: OrderService, private radService: RadService) {
    this.orderService.getUserOrders().subscribe(
      res => {
        this.orderList = res;
        if(this.orderList.length != 0) {
          this.emptyList = false;
        }
      }, err => {
        alert("error fetching user orders");
      }
    );
  }

  ngOnInit() {
  }

  onPreuzmiRad(id) {
    this.radService.downloadFileByradID(id).subscribe(
      res => {
        var blob = new Blob([res], {type: 'application/pdf'});
        var url= window.URL.createObjectURL(blob);
        window.open(url, "_blank");
      }, err => {
        alert("Error while download file");
      }
    );
  }

}
