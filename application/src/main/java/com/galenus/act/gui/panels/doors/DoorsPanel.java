package com.galenus.act.gui.panels.doors;

import com.galenus.act.classes.Door;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.managers.DoorManager;
import com.galenus.act.gui.components.IDoorTile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.galenus.act.classes.managers.DoorManager.doorMgr;
import static com.galenus.act.gui.Application.colorResource;

public class DoorsPanel extends JPanel implements GuiInterface {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<IDoorTile> doorTiles = new ArrayList<>();
    private JPanel doorsPanel;
    private JPanel statePanel;


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

    public void updateState(DoorManager.DoorState state) {
        switch (state) {
            case Ok:
                statePanel.setBackground(colorResource.readColor("Green"));
                break;
            case Warning:
                statePanel.setBackground(colorResource.readColor("Yellow"));
                break;
            case Error:
                statePanel.setBackground(colorResource.readColor("Red"));
                break;
        }
    }



    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        doorsPanel = new JPanel();
        statePanel = new JPanel();
        for (Door door : doorMgr().getDoorList()) {
            doorTiles.add(new IDoorTile(door));
        }

        // Test
        statePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        for (IDoorTile doorTile : doorTiles) {
            doorsPanel.add(doorTile);
        }

        add(doorsPanel, BorderLayout.CENTER);
        add(statePanel, BorderLayout.SOUTH);
    }

    @Override
    public void updateComponents(Object... args) {
    }

}
