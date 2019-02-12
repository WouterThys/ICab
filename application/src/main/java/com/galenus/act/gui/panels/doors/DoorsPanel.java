package com.galenus.act.gui.panels.doors;

import com.galenus.act.Application;
import com.galenus.act.classes.Door;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.managers.DoorManager;
import com.galenus.act.gui.components.IDoorTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static com.galenus.act.classes.managers.DoorManager.doorMgr;
import static com.galenus.act.Application.colorResource;

public class DoorsPanel extends JPanel implements GuiInterface, IDoorTile.DoorClickedListener {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<IDoorTile> doorTiles = new ArrayList<>();
    private JPanel doorsPanel;
    private JPanel statePanel;


    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private final Application application;

    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public DoorsPanel(Application application) {
        super();
        this.application = application;

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
            case ClosedWhileUnlocked:
            case ClosedWhileLocked:
                statePanel.setBackground(colorResource.readColor("Green"));
                break;
            case OpenWhileUnlocked:
                statePanel.setBackground(colorResource.readColor("Yellow"));
                break;
            case OpenWhileLocked:
                statePanel.setBackground(colorResource.readColor("Red"));
                break;
        }
    }



    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //
    // Door clicked
    //
    @Override
    public void onDoorClicked(MouseEvent e, Door door) {
        if (application != null) {
            if (e.getClickCount() == 4) {
                application.showDebugDialog();
            }
        }
    }

    //
    // Gui
    //
    @Override
    public void initializeComponents() {
        doorsPanel = new JPanel();
        statePanel = new JPanel();
        for (Door door : doorMgr().getDoorList()) {
            IDoorTile tile = new IDoorTile(door);
            tile.setClickListener(this);
            doorTiles.add(tile);
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
