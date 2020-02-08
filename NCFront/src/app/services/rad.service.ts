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

  postRad(rad, taskId) {
    return this.http.post("/api/rad/post/".concat(taskId), rad) as Observable<any>;
  }

  getAutorRadTasks() {
    return this.http.get('/api/rad/get/radTasks') as Observable<any>;
  }

}