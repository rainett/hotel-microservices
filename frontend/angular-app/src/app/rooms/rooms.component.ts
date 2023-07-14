import { Component } from '@angular/core';
import {Room} from "../room";
import {RoomsService} from "../rooms.service";

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
export class RoomsComponent {

  rooms: Room[] = [];

  constructor(private roomsService: RoomsService) {}

  ngOnInit() {
    this.roomsService.getAllRooms().then((rooms: Room[]) => {
      this.rooms = rooms;
    });
  }

}
