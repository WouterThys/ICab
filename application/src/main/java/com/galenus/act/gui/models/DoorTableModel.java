package com.galenus.act.gui.models;

import com.galenus.act.classes.Door;

import javax.swing.*;
import java.util.List;

import static com.galenus.act.Application.imageResource;

public class DoorTableModel extends IAbstractTableModel<Door> {

    private static final String[] COLUMN_NAMES = {"", "Name"};
    private static final Class[] COLUMN_CLASSES = {ImageIcon.class, String.class};

    private static final ImageIcon openIcon = imageResource.readImage("Doors.Table.Open");
    private static final ImageIcon closedIcon = imageResource.readImage("Doors.Table.Closed");


    public DoorTableModel(List<Door> doorList) {
        super(COLUMN_NAMES, COLUMN_CLASSES);

        setItemList(doorList);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Door door = getItemAt(rowIndex);

        if (door != null) {
            switch (columnIndex) {
                case -1:
                    return door;
                case 0: // Image
                    if (door.isOpen()) {
                        return openIcon;
                    } else {
                        return closedIcon;
                    }
                case 1: // Name
                    return "Door " + door.getId();
            }
        }
        return null;
    }
}
