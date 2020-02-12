import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-porudzbine',
  templateUrl: './porudzbine.component.html',
  styleUrls: ['./porudzbine.component.css']
})
export class PorudzbineComponent implements OnInit {

  emptyList: boolean = true;
  orderList: any;

  constructor(private orderService: OrderService) {
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

}
