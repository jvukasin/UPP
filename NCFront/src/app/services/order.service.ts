import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private httpClient: HttpClient) { }

  initMagazineOrder(casopis){
    return this.httpClient.post('/api/orders/casopis/init', casopis);
  }

  initMagazineSubscription(casopis) {
    return this.httpClient.post('/api/orders/casopis/initSub', casopis);
  }

  initPaperOrder(rad) {
    return this.httpClient.post('/api/orders/scPaper/init', rad);
  }

  getUserOrders() {
    return this.httpClient.get('/api/orders/userOrders');
  }

}
