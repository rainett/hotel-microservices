import { Injectable } from '@angular/core';
import {AxiosService} from "./axios.service";
import {Room} from "./room";

@Injectable({
  providedIn: 'root'
})
export class RoomsService {

  baseUrl: string = 'http://localhost:8080/api/v1/rooms';

  constructor(private axios: AxiosService) {}

  async getAllRooms(): Promise<Room[]> {
    let result = await this.axios.request('GET', this.baseUrl, null, null);
    console.log("request performed");
    console.log(result.data.content);
    return result.data.content;
  }

}
