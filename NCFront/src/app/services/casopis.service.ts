import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class CasopisService {

  constructor(private http: HttpClient) { }

  startCasopisProcess() {
    return this.http.get('/api/casopis/get') as Observable<any>
  }

  postCasopis(casopis, taskId) {
    return this.http.post("/api/casopis/post/".concat(taskId), casopis) as Observable<any>;
  }

}