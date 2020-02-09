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
    return this.http.get("/api/rad/getUrednikRadTasks") as Observable<any>;
  }

  UrednikObradaRada(o, taskId) {
    return this.http.post("/api/rad/obradaRada/".concat(taskId), o) as Observable<any>;
  }

  downloadFile(procesId) {
    const httpOptions = {
      'responseType'  : 'arraybuffer' as 'json'
    };
    return this.http.get("/api/rad/downloadFile/".concat(procesId), httpOptions) as Observable<any>;
  }

  getAutorIspravkaRadaTasks() {
    return this.http.get("/api/rad/getAutorIspravkaRadaTasks") as Observable<any>;
  }

  getUrednikIzborRecTasks() {
    return this.http.get("/api/rad/getUrednikIzborRecTasks") as Observable<any>;
  }

  getRecenzentiForm(taskId) {
    return this.http.get("/api/rad/getRecenzentiForm/".concat(taskId)) as Observable<any>;
  }

  postRecenzenti(o, taskId) {
    return this.http.post("/api/rad/postRecenzenti/".concat(taskId), o) as Observable<any>;
  }
  

}