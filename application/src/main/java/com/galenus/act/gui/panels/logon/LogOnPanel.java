package com.galenus.act.gui.panels.logon;

import com.galenus.act.classes.User;

import java.awt.*;
import java.util.List;

public class LogOnPanel extends LogOnPanelLayout {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public LogOnPanel() {
        super(5, 50);

        initializeComponents();
        initializeLayouts();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void setUsers(List<User> userList) {
        drawTiles(userList);
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //
    // Tiles
    //
    @Override
    public void onTileClick(User user) {
        updateComponents(user);
    }

    //
    // Key pad
    //
    @Override
    public void onDigitsEntered(String entered) {
        if (selectedUser != null) {
            if (selectedUser.isPinCorrect(entered)) {
                keyPad.setBackgroundColor(Color.GREEN);
            } else {
                keyPad.setBackgroundColor(Color.RED);
            }
        }
    }
}
