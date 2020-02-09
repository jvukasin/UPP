import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class RadService {

  constructor(private http: HttpClient) { }

  get(id){
    return this.http.get('/api/rad/'.concat(id));
  }

  getAll(){
    return this.http.get('/api/rad');
  }

  startRadProcess() {
    return this.http.get("/api/rad/get") as Observable<any>;
  }

  postIzabraniCasopis(dto, taskId) {
    return this.http.post("/api/rad/postIzabraniCasopis/".concat(taskId), dto) as Observable<any>;
  }

  postRad(rad, taskId) {
    return this.http.post("/api/rad/postRad/".concat(taskId), rad) as Observable<any>;
  }

  getAutorRadTasks() {
    return this.http.get('/api/rad/tasks/coauthor') as Observable<any>;
  }

  getRadForm(id) {
    return this.http.get("/api/rad/getRadForm/".concat(id)) as Observable<any>;
  }

  postFile(radId, data) {
    return this.http.put("/api/rad/uploadFile/".concat(radId), data, {responseType: 'text'}) as Observable<any>;
  }

  getFormFromTask(id) {
    return this.http.get("/api/rad/getFormFromTask/".concat(id)) as Observable<any>;
  }

  postKoautor(o, taskId) {
    return this.http.post("/api/rad/postKoautori/".concat(taskId), o) as Observable<any>;
  }

  getUrednikRadTasks() {
    
  }
  

}