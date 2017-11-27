package com.galenus.act.gui.panels.logon;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.UserListener;

import java.util.List;

public class LogOnPanel extends LogOnPanelLayout {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private UserListener userListener;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public LogOnPanel(UserListener userListener) {
        super(5, 50);

        this.userListener = userListener;

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
        if (userListener != null) {
            userListener.onUserSelected(user);
        }
    }
}
