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

  getAdminTasks(){
    return this.http.get('/api/admin/get/tasks') as Observable<any>
  }

  getAdminForm(taskId){
    return this.http.get('/api/admin/task/claim/'.concat(taskId)) as Observable<any>
  }

  potvrdaRecenzenta(taskId, obj) {
    return this.http.post("/api/admin/potvrdaRecenzenta/".concat(taskId), obj) as Observable<any>;
  }

  claimTask(taskId){
    return this.http.post('/api/registration/tasks/claim/'.concat(taskId), null) as Observable<any>
  }

  completeTask(taskId){
    return this.http.post('/api/users/tasks/complete/'.concat(taskId), null) as Observable<any>
  }

}