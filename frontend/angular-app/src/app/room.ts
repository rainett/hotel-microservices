
export interface Room {
  id: number;
  number?: number;
  floor?: number;
  positionOnFloor?: number;
  roomType?: RoomType;
  roomState?: RoomState;
  residentsIds: number[];
  residentsNumber: number;
  price: number;
  createdAt: Date;
}

export enum RoomType {
  SUITE, DELUXE, STANDARD
}

export enum RoomState {
  VACANT, OCCUPIED, RESERVED, SERVICE
}
