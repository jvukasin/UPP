import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class KPService {

  constructor(private httpClient: HttpClient) { }

  createPlan(id){
    return this.httpClient.get('/api/kp/createPlan/'.concat(id));
  }

  registerMagazinSeller(magazine) {
    return this.httpClient.post('/api/kp/registration', magazine);
  }

  reviewRegistration(magazine) {
    return this.httpClient.post('/api/kp/registration/review', magazine);
  }

  getPlans(magazienId) {
    return this.httpClient.get('/api/kp/getPlans/'.concat(magazienId), {responseType: 'text'});
  }

  getUserAgreements() {
    return this.httpClient.get('/api/kp/getUserAgreements/');
  }

  cancelAgreement(agrID, sellerID) {
    return this.httpClient.get('/api/kp/cancelAgreement/'.concat(agrID).concat("/").concat(sellerID), {responseType: 'text'});
  }

}
