package com.galenus.act.gui.panels.logon;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.gui.components.IUserTile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract class LogOnPanelLayout extends JPanel implements
        GuiInterface,
        IUserTile.OnTileClickListener {



    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JPanel gridPanel;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private int rows = -1;
    private int cols = -1;


    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    LogOnPanelLayout(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private IUserTile createTile(User user) {
        IUserTile tileView = new IUserTile(user);
        tileView.addOnTileClickListener(this);
        return tileView;
    }

    void drawTiles(List<User> userList) {
        gridPanel.removeAll();
        for (User user : userList) {
            gridPanel.add(createTile(user));
        }
        if (userList.size() > 0) {
            updateComponents();
        }
    }

//    private boolean findUser(User user) {
//        for (IUserTile userTile : userTiles) {
//            if (userTile.getUser().equals(user)) {
//                return true;
//            }
//        }
//        return false;
//    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        gridPanel = new JPanel(new GridLayout(rows,cols));
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        // Grid
        JScrollPane scrollPane = new JScrollPane(gridPanel);

        // Add
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... args) {

    }
}
