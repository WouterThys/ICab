package com.galenus.act.classes.managers;

import com.galenus.act.classes.Door;
import com.galenus.act.serial.SerialMessage;

import java.util.ArrayList;
import java.util.List;

public class DoorManager {

    public enum DoorState {
        Ok,
        Warning,
        Error
    }

    private static final DoorManager Instance = new DoorManager();
    public static DoorManager doorMgr() {
        return Instance;
    }
    private DoorManager() {}

    private List<Door> doorList = new ArrayList<>();

    public void init(int doorCount) {
        doorList.clear();
        for (int d = 0; d < doorCount; d++) {
            doorList.add(new Door(d));
        }
    }

    public void unlockDoors() {
        for (Door door : doorList) {
            door.setLocked(false);
        }
    }

    public void lockDoors() {
        for (Door door : doorList) {
            door.setLocked(true);
        }
    }

    public Door updateDoor(SerialMessage message) {
        if (message.getCommand().contains("D")) {
            for (Door door : doorList) {
                String idString = String.valueOf(door.getId());
                if (message.getCommand().contains(idString)) {
                    switch (message.getMessage()) {
                        case "O": door.setOpen(true); break;
                        case "C": door.setOpen(false); break;
                    }
                    return door;
                }
            }
        }
        return null;
    }

    public DoorState getDoorState() {
        for (Door door : getDoorList()) {
            if (door.isOpen() && door.isLocked()) {
                return DoorState.Error;
            }
        }

        // TODO: door state overtime

        return DoorState.Ok;
    }

    public List<Door> getDoorList() {
        return doorList;
    }

}
