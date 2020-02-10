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

  getRecenzentRecenziranjeTasks() {
    return this.http.get("/api/rad/getRecenzentRecenziranjeTasks") as Observable<any>;
  }

  postRecenzija(o, taskId) {
    return this.http.post("/api/rad/postRecenzija/".concat(taskId), o) as Observable<any>;
  }

  getUrednikIzborRecOpetTasks() {
    return this.http.get("/api/rad/getUrednikIzborRecOpetTasks") as Observable<any>;
  }

  getNoviRecenzentForm(taskId) {
    return this.http.get("/api/rad/getNoviRecenzentForm/".concat(taskId)) as Observable<any>;
  }

  postNoviRecenzent(o, taskId) {
    return this.http.post("/api/rad/postNoviRecenzent/".concat(taskId), o) as Observable<any>;
  }

  getUrednikPregledaTasks() {
    return this.http.get("/api/rad/getUrednikPregledaTasks") as Observable<any>;
  }

  getPregledForm(taskId) {
    return this.http.get("/api/rad/getPregledForm/".concat(taskId)) as Observable<any>;
  }

  postOdlukaUrednika(o, taskId) {
    return this.http.post("/api/rad/postOdlukaUrednika/".concat(taskId), o) as Observable<any>;
  }

  getAutorKomentariTasks() {
    return this.http.get("/api/rad/getAutorKomentariTasks") as Observable<any>;
  }

  getKomentarIspravkaForm(taskId) {
    return this.http.get("/api/rad/getKomentarIspravkaForm/".concat(taskId)) as Observable<any>;
  }

  postIspravkeRada(o, taskId) {
    return this.http.post("/api/rad/postIspravkeRada/".concat(taskId), o) as Observable<any>;
  }

  getAutorFinalTasks() {
    return this.http.get("/api/rad/getAutorFinalTasks") as Observable<any>;
  }
  
  getUrednikFinalTasks() {
    return this.http.get("/api/rad/getUrednikFinalTasks") as Observable<any>;
  }

  getFinalUrednikForm(taskId) {
    return this.http.get("/api/rad/getFinalUrednikForm/".concat(taskId)) as Observable<any>;
  }
  
  postFinalUrednik(o, taskId) {
    return this.http.post("/api/rad/postFinalUrednik/".concat(taskId), o) as Observable<any>;
  }

  //mock download rada brisi
  downloadFileByradID(radID) {
    const httpOptions = {
      'responseType'  : 'arraybuffer' as 'json'
    };
    return this.http.get("/api/rad/downloadFileByRadID/".concat(radID), httpOptions) as Observable<any>;
  }

}