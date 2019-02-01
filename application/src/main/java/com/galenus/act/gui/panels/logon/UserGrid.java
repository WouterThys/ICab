package com.galenus.act.gui.panels.logon;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.UserListener;
import com.galenus.act.gui.components.IUserTile;

import java.awt.event.MouseEvent;
import java.util.List;

public class UserGrid extends UserGridLayout {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private UserListener userListener;
    private IUserTile selectedTile;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public UserGrid(UserListener userListener) {
        super(5, 100);

        this.userListener = userListener;

        initializeComponents();
        initializeLayouts();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void setUsers(List<User> userList) {
        //drawTiles(userList);
        drawTabbedTiles(userList);
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //
    // Tiles
    //
    @Override
    public void onTileClick(MouseEvent e, IUserTile tile) {
        if (tile != null) {
            if (userListener != null) {
                userListener.onUserSelected(tile.getUser());
            }
            if (selectedTile != null) {
                selectedTile.setSelected(false);
            }
            selectedTile = tile;
            selectedTile.setSelected(true);
        }
    }
}
