import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  categories = [];
  languages = [];
  books = [];

  constructor(private http: HttpClient) {
  }


  startProcess(){
    return this.http.get('/api/users/get') as Observable<any>
  }

  getTasks(processInstance : string){
    return this.http.get('/api/users/get/tasks/'.concat(processInstance)) as Observable<any>
  }

  getAdminRecTasks(){
    return this.http.get('/api/admin/get/recTasks') as Observable<any>
  }

  getAdminUrdTasks(){
    return this.http.get('/api/admin/get/urdTasks') as Observable<any>
  }
  
  getAdminForm(taskId){
    return this.http.get('/api/admin/task/claim/'.concat(taskId)) as Observable<any>
  }

  potvrdaRecenzenta(taskId, obj) {
    return this.http.post("/api/admin/potvrdaRecenzenta/".concat(taskId), obj) as Observable<any>;
  }

  potvrdaCasopisa(taskId, obj) {
    return this.http.post("/api/admin/potvrdaCasopisa/".concat(taskId), obj) as Observable<any>;
  }

}