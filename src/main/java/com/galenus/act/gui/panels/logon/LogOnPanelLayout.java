package com.galenus.act.gui.panels.logon;

import com.galenus.act.classes.User;
import com.galenus.act.gui.GuiInterface;
import com.galenus.act.gui.components.IKeyPad;
import com.galenus.act.gui.components.IUserTile;

import javax.swing.*;
import java.awt.*;
import java.util.List;

abstract class LogOnPanelLayout extends JPanel implements GuiInterface, IUserTile.OnTileClickListener {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JPanel gridPanel;

    private JLabel userNameField;

    private IKeyPad keyPad;

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

    private void updateUserInfo(User user) {
        if (user != null) {
            userNameField.setText(user.getFirstName());
            keyPad.clear();
        } else {
            userNameField.setText("");
            keyPad.clear();
        }
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel();

        // Add
        JPanel p = new JPanel(new BorderLayout());
        p.add(userNameField, BorderLayout.NORTH);
        p.add(keyPad, BorderLayout.CENTER);


        panel.add(p);

        return panel;
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        gridPanel = new JPanel(new GridLayout(rows,cols));

        userNameField = new JLabel("", JLabel.CENTER);
        Font f = userNameField.getFont();
        userNameField.setFont(new Font(f.getName(), Font.BOLD, 50));
        userNameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        keyPad = new IKeyPad();
        keyPad.setPreferredSize(new Dimension(300,400));


    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        // Grid
        JScrollPane scrollPane = new JScrollPane(gridPanel);

        // User
        JPanel userPanel = createUserPanel();

        // Add
        add(scrollPane, BorderLayout.CENTER);
        add(userPanel, BorderLayout.EAST);
    }

    @Override
    public void updateComponents(Object... args) {
        if (args.length > 0 && args[0] != null) {
            selectedUser = (User) args[0];
        } else {
            selectedUser = null;
        }

        updateUserInfo(selectedUser);
    }
}
