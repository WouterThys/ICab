package com.galenus.act.gui.panels.doors;

import com.galenus.act.classes.Door;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.gui.components.IDoorTile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.galenus.act.classes.managers.DoorManager.doorMgr;

public class DoorsPanel extends JPanel implements GuiInterface {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<IDoorTile> doorTiles = new ArrayList<>();

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public DoorsPanel() {
        initializeComponents();
        initializeLayouts();
        updateComponents();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void updateDoor(Door door) {
        if (door != null) {
            for (IDoorTile doorTile : doorTiles) {
                if (doorTile.getDoor().equals(door)) {
                    doorTile.updateComponents();
                }
            }
        }
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        for (Door door : doorMgr().getDoorList()) {
            doorTiles.add(new IDoorTile(door));
        }
    }

    @Override
    public void initializeLayouts() {
        for (IDoorTile doorTile : doorTiles) {
            add(doorTile);
        }
    }

    @Override
    public void updateComponents(Object... args) {
    }

}
