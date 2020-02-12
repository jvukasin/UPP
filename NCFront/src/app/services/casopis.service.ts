import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class CasopisService {

  constructor(private http: HttpClient) { }

  get(id){
    return this.http.get('/api/casopis/casopisi/'.concat(id));
  }

  getAll(){
    return this.http.get('/api/casopis/casopisi');
  }


  startCasopisProcess() {
    return this.http.get("/api/casopis/get") as Observable<any>;
  }

  postCasopis(casopis, taskId) {
    return this.http.post("/api/casopis/post/".concat(taskId), casopis) as Observable<any>;
  }

  getOdborForma(id) {
    return this.http.get("/api/casopis/get/UredRec/".concat(id)) as Observable<any>;
  }

  postOdbor(odbor, taskId) {
    return this.http.post("/api/casopis/post/UredRec/".concat(taskId), odbor) as Observable<any>;
  }

  postIzmenaOdbor(odbor, taskId) {
    return this.http.post("/api/casopis/post/UredRecIzmena/".concat(taskId), odbor) as Observable<any>;
  }

  getUrednikCasopisTasks(){
    return this.http.get('/api/casopis/get/casopisTasks') as Observable<any>;
  }

  getIspravkaForm(taskId){
    return this.http.get('/api/casopis/get/ispravkaForm/'.concat(taskId)) as Observable<any>
  }

  posaljiIspravljeniCasopis(taskId, obj) {
    return this.http.post("/api/casopis/posaljiIspravljeniCasopis/".concat(taskId), obj) as Observable<any>;
  }

  getAllByUrednik() {
    return this.http.get('/api/casopis/getAllByUrednik') as Observable<any>;
  }

  proveriPretplacen(casID) {
    return this.http.get('/api/casopis/isSubbed/'.concat(casID), {responseType: 'text'}) as Observable<any>;
  }

}