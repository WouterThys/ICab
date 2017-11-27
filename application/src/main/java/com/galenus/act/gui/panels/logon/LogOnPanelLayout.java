package com.galenus.act.gui.panels.logon;

import com.galenus.act.classes.User;
import com.galenus.act.gui.GuiInterface;
import com.galenus.act.gui.components.IKeyPad;
import com.galenus.act.gui.components.IUserTile;

import javax.swing.*;
import java.awt.*;
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

    User selectedUser;

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
        return  tileView;
    }

    void drawTiles(List<User> userList) {
        gridPanel.removeAll();
        for (User user : userList) {
            gridPanel.add(createTile(user));
        }
        if (userList.size() > 0) {
            updateComponents(userList.get(0));
        }
    }


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
        if (args.length > 0 && args[0] != null) {
            selectedUser = (User) args[0];
        } else {
            selectedUser = null;
        }
    }
}
