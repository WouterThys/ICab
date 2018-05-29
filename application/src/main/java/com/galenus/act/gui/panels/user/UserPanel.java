package com.galenus.act.gui.panels.user;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.interfaces.UserListener;
import com.galenus.act.gui.components.IKeyPad;
import com.galenus.act.gui.components.ILabel;
import com.galenus.act.gui.components.ITimerPanel;
import com.galenus.act.utils.DateUtils;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel implements GuiInterface, IKeyPad.KeyPadListener {

    private static final String VIEW_KEYPAD = "KeyPad";
    private static final String VIEW_TIMER = "Timer";

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JLabel userNameLbl;
    private JLabel userLastNameLbl;
    private JLabel userLastLogInLbl;
    private JLabel userAvatarLbl;

    private CardLayout cardLayout;
    private JPanel centerPanel;
    private IKeyPad keyPad;
    private ITimerPanel timerPanel;

    private String currentView = "";


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
    public void updateTimer(String newTime) {
        timerPanel.updateComponents(newTime);
    }

    public void updateTimerView() {
        timerPanel.updateEnabledComponents();
    }

    private void showView(String key) {
        updateTimer(" ");
        if (!currentView.equals(key)) {
            cardLayout.show(centerPanel, key);
        }
        currentView = key;
    }

    private void updateUserInfo(User user) {
        if (user != null) {
            userNameLbl.setText(user.getFirstName());
            userLastNameLbl.setText(user.getLastName());
            userAvatarLbl.setIcon(user.getAvatar());
            userLastLogInLbl.setText(DateUtils.formatDateTime(user.getLastLogIn()));

            if (user.isLoggedIn()) {
                showView(VIEW_TIMER);
            } else {
                showView(VIEW_KEYPAD);
            }

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
        JPanel userPnl = new JPanel(new BorderLayout());
        JPanel infoPnl = new JPanel(new BorderLayout());

        // Info
        Box box = Box.createVerticalBox();
        box.add(userNameLbl);
        box.add(userLastNameLbl);
        box.add(userLastLogInLbl);
        infoPnl.add(box);

//        infoPnl.setLayout(new BoxLayout(infoPnl, BoxLayout.Y_AXIS));
//        infoPnl.add(userNameLbl);
//        infoPnl.add(userLastNameLbl);
//        infoPnl.add(userLastLogInLbl);

        // User
        userPnl.add(userAvatarLbl, BorderLayout.WEST);
        userPnl.add(infoPnl, BorderLayout.CENTER);
        userPnl.setBorder(BorderFactory.createEmptyBorder(50, 10, 20, 20));

        return userPnl;
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

        userNameLbl.setAlignmentX(SwingConstants.RIGHT);
        userLastNameLbl.setAlignmentX(SwingConstants.RIGHT);
        userLastLogInLbl.setAlignmentX(SwingConstants.RIGHT);

        // Key pad
        keyPad = new IKeyPad();
        keyPad.addKeyPadListener(this);
        keyPad.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        keyPad.setPreferredSize(new Dimension(400, 500));

        // Timer
        timerPanel = new ITimerPanel();
        timerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        timerPanel.setPreferredSize(new Dimension(300, 400));

        // Cards
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        // User
        JPanel userPanel = createUserPanel();

        // Center
        JPanel t = new JPanel(new BorderLayout());
        JPanel k = new JPanel();

        t.add(timerPanel, BorderLayout.CENTER);
        k.add(keyPad);

        centerPanel.add(VIEW_KEYPAD, k);
        centerPanel.add(VIEW_TIMER, t);
        cardLayout.show(centerPanel, VIEW_KEYPAD);

        // Add
        add(userPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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
                showView(VIEW_TIMER);
            } else {
                keyPad.setBackgroundColor(Color.RED);
                showView(VIEW_KEYPAD);
            }
        }
    }
}

