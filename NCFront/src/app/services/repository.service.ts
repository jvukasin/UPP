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
    return this.http.get('http://localhost:8080/registration/get') as Observable<any>
  }

  getTasks(processInstance : string){
    return this.http.get('http://localhost:8080/registration/get/tasks/'.concat(processInstance)) as Observable<any>
  }

  getAdminTasks(){
    return this.http.get('http://localhost:8080/admin/get/tasks') as Observable<any>
  }

  claimTask(taskId){
    return this.http.post('http://localhost:8080/registration/tasks/claim/'.concat(taskId), null) as Observable<any>
  }

  completeTask(taskId){
    return this.http.post('http://localhost:8080/registration/tasks/complete/'.concat(taskId), null) as Observable<any>
  }

}