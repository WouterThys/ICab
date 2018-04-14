package com.galenus.act.gui.components;

import com.galenus.act.classes.Door;
import com.galenus.act.classes.interfaces.GuiInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.galenus.act.Application.imageResource;

public class IDoorTile extends JPanel implements GuiInterface, MouseListener {

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

    private boolean isSelected = false;
    private Color background;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public IDoorTile(Door door) {
        this.door = door;

        addMouseListener(this);
        background = this.getBackground();

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

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            this.setBackground(Color.gray);
        } else {
            this.setBackground(background);
        }
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


    //
    // Mouse listener
    //
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(Color.gray.brighter());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setSelected(isSelected);
    }
}
