import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  fetchUsers() {
    return this.http.get("http://localhost:8080/user/fetch") as Observable<any>;
  }

  registerUser(user, taskId) {
    return this.http.post("http://localhost:8080/registration/post/".concat(taskId), user) as Observable<any>;
  }

  verifyUser(pcs, usr) {
    return this.http.get("http://localhost:8080/registration/verify/".concat(pcs).concat("/").concat(usr));
  }
}