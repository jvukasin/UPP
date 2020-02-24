import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class SearchService {

  constructor(private http: HttpClient) { }

  simpleSearch(search){
    return this.http.post('/api/elastic/simpleSearch', search);
  }

  advancedSearch(search) {
    return this.http.post('/api/elastic/advancedSearch', search);
  }

}