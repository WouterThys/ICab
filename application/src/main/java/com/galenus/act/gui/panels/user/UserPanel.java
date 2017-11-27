package com.galenus.act.gui.panels.user;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.interfaces.UserListener;
import com.galenus.act.gui.components.IKeyPad;
import com.galenus.act.gui.components.ILabel;
import com.galenus.act.utils.DateUtils;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel implements GuiInterface, IKeyPad.KeyPadListener {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JLabel userNameLbl;
    private JLabel userLastNameLbl;
    private JLabel userLastLogInLbl;
    private JLabel userAvatarLbl;
    private IKeyPad keyPad;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private UserListener userListener;

    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public UserPanel(UserListener userListener) {
        this.userListener = userListener;
        initializeComponents();
        initializeLayouts();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void updateUserInfo(User user) {
        if (user != null) {
            userNameLbl.setText(user.getFirstName());
            userLastNameLbl.setText(user.getLastName());
            userAvatarLbl.setIcon(user.getAvatar());
            userLastLogInLbl.setText(DateUtils.formatDateTime(user.getLastLogIn()));

            keyPad.clear();
        } else {
            userNameLbl.setText("");
            userLastNameLbl.setText("");
            userAvatarLbl.setIcon(null);
            userLastLogInLbl.setText("");

            keyPad.clear();
        }
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel userPnl = new JPanel(new BorderLayout());
        JPanel infoPnl = new JPanel();

        // Info
        infoPnl.setLayout(new BoxLayout(infoPnl, BoxLayout.Y_AXIS));
        infoPnl.add(userNameLbl);
        infoPnl.add(userLastNameLbl);
        infoPnl.add(userLastLogInLbl);

        // User
        userPnl.add(userAvatarLbl, BorderLayout.WEST);
        userPnl.add(infoPnl, BorderLayout.CENTER);
        userPnl.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // Add
        JPanel p = new JPanel(new BorderLayout());
        JPanel k = new JPanel();
        k.add(keyPad);
        p.add(userPnl, BorderLayout.NORTH);
        p.add(k, BorderLayout.CENTER);

        panel.add(p, BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(300,400));

        return panel;
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        userNameLbl = new JLabel();
        Font f = userNameLbl.getFont();
        userNameLbl.setFont(new Font(f.getName(), Font.BOLD, 50));

        userLastNameLbl = new ILabel();
        userLastLogInLbl = new ILabel();
        userAvatarLbl = new ILabel();

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
            updateUserInfo((User) args[0]);
        } else {
            updateUserInfo(null);
        }
    }

    @Override
    public void onDigitsEntered(String entered) {
        if (userListener != null) {
            if (userListener.onPasswordEntered(entered)) {
                System.out.println("Login user");
//                keyPad.setBackgroundColor(Color.GREEN);
//            } else {
//                keyPad.setBackgroundColor(Color.RED);
            }
        }
    }
}

