package com.galenus.act.gui.panels.user;

import com.galenus.act.classes.User;
import com.galenus.act.gui.GuiInterface;
import com.galenus.act.gui.components.IKeyPad;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel implements GuiInterface, IKeyPad.KeyPadListener {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JLabel userNameField;
    IKeyPad keyPad;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    User selectedUser;

    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public UserPanel() {

    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
        JPanel panel = new JPanel(new BorderLayout());

        JPanel namePnl = new JPanel();
        namePnl.add(userNameField);
        namePnl.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // Add
        JPanel p = new JPanel(new BorderLayout());
        p.add(namePnl, BorderLayout.NORTH);
        p.add(keyPad, BorderLayout.CENTER);

        panel.add(p, BorderLayout.NORTH);

        return panel;
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        userNameField = new JLabel("", JLabel.CENTER);
        Font f = userNameField.getFont();
        userNameField.setFont(new Font(f.getName(), Font.BOLD, 50));
        userNameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        keyPad = new IKeyPad();
        keyPad.addKeyPadListener(this);
        keyPad.setPreferredSize(new Dimension(300,400));
        keyPad.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());
        // User
        JPanel userPanel = createUserPanel();

        // Add
        add(userPanel, BorderLayout.CENTER);
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

    @Override
    public void onDigitsEntered(String entered) {

    }
}

