package com.galenus.act.gui.components;

import com.galenus.act.classes.Door;
import com.galenus.act.classes.interfaces.GuiInterface;

import javax.swing.*;
import java.awt.*;

import static com.galenus.act.gui.Application.imageResource;

public class IDoorTile extends JPanel implements GuiInterface {

    private static final ImageIcon openIcon = imageResource.readImage("Doors.Open");
    private static final ImageIcon closedIcon = imageResource.readImage("Doors.Closed");

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
   private JLabel doorLbl;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Door door;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public IDoorTile(Door door) {
        this.door = door;

        initializeComponents();
        initializeLayouts();
        updateComponents();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public Door getDoor() {
        return door;
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
       doorLbl = new ILabel("", ILabel.CENTER);
       doorLbl.setIcon(closedIcon);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        add(doorLbl, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(3,1,3,1));
    }

    @Override
    public void updateComponents(Object... args) {
        if (door != null) {
            if (door.isOpen()) {
                doorLbl.setIcon(openIcon);
            } else {
                doorLbl.setIcon(closedIcon);
            }
            repaint();
        }
    }
}
