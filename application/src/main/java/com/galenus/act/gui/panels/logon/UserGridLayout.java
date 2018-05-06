package com.galenus.act.gui.panels.logon;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.gui.components.IUserTile;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

abstract class UserGridLayout extends JPanel implements
        GuiInterface,
        IUserTile.OnTileClickListener {



    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JPanel gridPanel;
    private JPanel tabPanel;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private int rows = -1;
    private int cols = -1;


    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    UserGridLayout(int rows, int cols) {
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

    void drawTabbedTiles(List<User> userList) {
        tabPanel.removeAll();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont( new Font( "Dialog", Font.BOLD|Font.ITALIC, 28 ) );
        JScrollPane scrollPane = new JScrollPane(gridPanel);

        userList.sort(Comparator.comparing(User::getFirstName));

        // "All" tab
        drawTiles(userList);
        tabbedPane.addTab(" All ", scrollPane);

        char firstChar = ' ';
        JPanel contentPnl = new JPanel();
        for (User user : userList) {
            char fc;

            if (user.getFirstName().isEmpty()) {
                fc = ' ';
            } else {
                fc = user.getFirstName().charAt(0);
            }

            // Add tabs
            if (firstChar != fc) {
                contentPnl = new JPanel();
                String t = "  " + String.valueOf(fc).toUpperCase() + "  ";
                tabbedPane.addTab(t, contentPnl);
                firstChar = fc;
            }

            // Add tiles
            IUserTile tile = createTile(user);
            tile.setPreferredSize(new Dimension(120,200));
            contentPnl.add(tile);
        }

        tabPanel.add(tabbedPane);
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

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        gridPanel = new JPanel(new GridLayout(rows,cols));
        tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1,1,1,1),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 1),
                        BorderFactory.createEmptyBorder(10,0,10,0)
                )
        ));
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        add(tabPanel, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... args) {

    }
}
