package io.rainett.roommicroservice.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long id) {
        super("Room with id = [" + id + "] was not found");
    }
}
