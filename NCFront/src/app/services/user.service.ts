import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  registerUser(user, taskId) {
    return this.http.post("/api/users/post/".concat(taskId), user) as Observable<any>;
  }

  verifyUser(pcs, usr) {
    return this.http.get("/api/users/verify/".concat(pcs).concat("/").concat(usr));
  }

  getUser(){
    return this.http.get('/api/users/getUser');  
  }
}