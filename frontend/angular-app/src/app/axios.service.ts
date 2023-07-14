import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor() {
    axios.defaults.headers.post['Content-Type'] = "application/json"
  }

  request(method: string, url: string, data: any, headers: any): Promise<any> {
    const config = {
      method: method,
      url: url,
      data: data,
      headers: headers
    };

    return axios(config);
  }

}

