package com.mohitprasad.codeeditor.service;

import com.mohitprasad.codeeditor.model.dto.CreateRoomRequest;
import com.mohitprasad.codeeditor.model.dto.CreateRoomResponse;
import com.mohitprasad.codeeditor.model.dto.RoomInfoResponse;
import com.mohitprasad.codeeditor.model.entity.Room;

public interface RoomService {
    CreateRoomResponse createRoom(CreateRoomRequest request);
    RoomInfoResponse getRoomInfo(String roomCode);
    Room getRoomByCode(String roomCode);
    void closeRoom(String roomCode);
    boolean roomExists(String roomCode);
}
