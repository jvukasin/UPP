import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private httpClient: HttpClient) { }

  initMagazineOrder(casopisDTO){
    return this.httpClient.post('/api/orders/magazine/init', casopisDTO);
  }

  initMagazineSubscription(casopisDTO) {
    return this.httpClient.post('/api/orders/magazine/initSub', casopisDTO);
  }

  initPaperOrder(rad) {
    return this.httpClient.post('/api/orders/scPaper/init', rad);
  }

}
