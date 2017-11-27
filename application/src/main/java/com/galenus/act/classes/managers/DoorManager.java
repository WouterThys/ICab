package com.galenus.act.classes.managers;

import com.galenus.act.classes.Door;
import com.galenus.act.serial.SerialMessage;

import java.util.ArrayList;
import java.util.List;

public class DoorManager {

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

    public List<Door> getDoorList() {
        return doorList;
    }

}
