import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class MockService {

  constructor(private http: HttpClient) { }

  getForm(id) {
    return this.http.get("/api/mock/getForm/".concat(id)) as Observable<any>;
  }

  uplatiClanarinu(o, taskId) {
    return this.http.post("/api/mock/uplatiClanarinu/".concat(taskId), o) as Observable<any>;
  }
  

}