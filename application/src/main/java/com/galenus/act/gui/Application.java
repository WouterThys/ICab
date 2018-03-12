package com.galenus.act.gui;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.Main;
import com.galenus.act.classes.Door;
import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.interfaces.SerialListener;
import com.galenus.act.classes.interfaces.UserListener;
import com.galenus.act.classes.interfaces.WebCallListener;
import com.galenus.act.classes.managers.DoorManager.DoorState;
import com.galenus.act.gui.dialogs.initializationdialog.InitializationDialog;
import com.galenus.act.gui.dialogs.logsdialog.LogsDialog;
import com.galenus.act.gui.panels.doors.DoorsPanel;
import com.galenus.act.gui.panels.inventory.InventoryPanel;
import com.galenus.act.gui.panels.logon.LogOnPanel;
import com.galenus.act.gui.panels.user.UserPanel;
import com.galenus.act.serial.SerialMessage;
import com.galenus.act.utils.resources.ColorResource;
import com.galenus.act.utils.resources.ImageResource;
import org.ksoap2.serialization.SoapObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import static com.galenus.act.classes.managers.DoorManager.doorMgr;
import static com.galenus.act.classes.managers.UserManager.usrMgr;
import static com.galenus.act.serial.SerialManager.serMgr;
import static com.galenus.act.web.WebManager.*;

public class Application extends JFrame implements
        GuiInterface,
        SerialListener,
        WebCallListener,
        UserListener {

    public static ImageResource imageResource;
    public static ColorResource colorResource;

    private static final String VIEW_MAIN = "Main";
    private static final String VIEW_INVENTORY = "Inventory";

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JMenuBar menuBar;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private LogOnPanel logOnPanel;
    private InventoryPanel inventoryPanel;

    private UserPanel userPanel;
    private DoorsPanel doorsPanel;

    private DoorState previousDoorState = DoorState.ClosedWhileLocked;
    private boolean alarmSend = false;


    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private Action showMenuAction = new AbstractAction("ShowMenu") {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isVisible = (getJMenuBar() != null);
            setMenuVisible(!isVisible);
        }
    };

    private Action showUserPinsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            usrMgr().printAllUserPins();
        }
    };

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Application(String startUpPath) {
        Application.imageResource = new ImageResource("", "icons.properties");
        Application.colorResource = new ColorResource("", "colors.properties");

        // Doors
        doorMgr().init(Main.DOOR_COUNT);
        usrMgr().init(Main.USER_LOGON_TIME, this);

        // Init gui
        initializeComponents();
        initializeLayouts();

        // Start initialize:
        SwingUtilities.invokeLater(() -> {
            InitializationDialog dialog = new InitializationDialog(this, "Initializing", this, this);
            dialog.showDialog();
            doDoorLogic(null);
        });
    }

    private void doDoorLogic(User user) {
        DoorState doorState = doorMgr().getDoorState();
        doorsPanel.updateState(doorState);

        // Doors were locked but someone opened it
        if ((previousDoorState == DoorState.ClosedWhileLocked) && (doorState == DoorState.OpenWhileLocked)) {
            serMgr().sendUnlockAll(); // Unlock all doors
            serMgr().sendAlarm(2);
            webMgr().alarmDoorForced();
        }

        // We were in error but all is OK again
        if ((previousDoorState == DoorState.OpenWhileLocked) && (doorState == DoorState.ClosedWhileLocked)) {
            serMgr().sendLockAll(); // Lock again
            serMgr().sendAlarm(0);
            webMgr().doorClose();
        }

        // Ready to log off
        if (user != null && user.isOverTime()) {
            if (doorState == DoorState.ClosedWhileUnlocked) {
                webMgr().logOff(user);
                serMgr().sendAlarm(0);
                alarmSend = false;
            } else {
                if (!alarmSend) {
                    serMgr().sendAlarm(1);
                    webMgr().alarmDoorNotClosed();
                    alarmSend = true;
                }
            }
        }


        previousDoorState = doorState;
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void startWait(Component component) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void stopWait(Component component) {
        component.setCursor(Cursor.getDefaultCursor());
    }

    private void webRegistered() {
        // Fetch users
        webMgr().getDeviceUsers();
    }

    private void webReceivedUsers(Vector response) {
        try {
            // Create user list
            SoapObject users = (SoapObject) response.get(1);
            for (int i = 0; i < users.getPropertyCount(); i++) {
                usrMgr().addUser(new User((SoapObject) users.getProperty(i)));
            }
            // Update components
            updateComponents();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setMenuVisible(boolean visible) {
        if (visible) {
            setJMenuBar(menuBar);
        } else {
            setJMenuBar(null);
        }
        pack();
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //
    // Gui stuff
    //
    @Override
    public void initializeComponents() {
        // Panels
        logOnPanel = new LogOnPanel(this);
        inventoryPanel = new InventoryPanel();
        userPanel = new UserPanel(this);
        doorsPanel = new DoorsPanel();

        // Cards
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(VIEW_MAIN, logOnPanel);
        mainPanel.add(VIEW_INVENTORY, inventoryPanel);

        // Menu panel
        menuBar = new JMenuBar();
        JMenu serialMenu = new JMenu("Serial");
        JMenuItem serialLogs = new JMenuItem("Serial logs");
        serialLogs.addActionListener(e -> {
            LogsDialog dialog = new LogsDialog(this, "Serial logs", serMgr().getSerialPort());
            dialog.showDialog();
        });

        serialMenu.add(serialLogs);
        menuBar.add(serialMenu);

        // Key strokes
        this.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK),
                "debugModeKey");
        this.getRootPane().getActionMap().put("debugModeKey", showMenuAction);

        this.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_U,
                InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                "usersKey");
        this.getRootPane().getActionMap().put("usersKey", showUserPinsAction);

    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        // East panel
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(userPanel, BorderLayout.CENTER);
        eastPanel.add(doorsPanel, BorderLayout.SOUTH);

        // Add
        add(mainPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

        if (Main.DEBUG_MODE) {
            setMenuVisible(true);
        }

        pack();
    }

    @Override
    public void updateComponents(Object... args) {
        // Main layout
        cardLayout.show(mainPanel, VIEW_MAIN);

        // Users
        logOnPanel.setUsers(usrMgr().getUserList());

        // Other

        pack();

    }

    //
    // User stuff
    //
    @Override
    public void onUserSelected(User user) {
        usrMgr().setSelectedUser(user);
        userPanel.updateComponents(user);
    }

    @Override
    public boolean onPasswordEntered(String password) {
        if (usrMgr().getSelectedUser() != null && usrMgr().logInUser(password)) {
            webMgr().logOn(usrMgr().getSelectedUser());
            return true;
        }
        return false;
    }

    @Override
    public void onUserShouldLogOff(User user) {
        doDoorLogic(user);
//        if (user != null) {
//            webMgr().logOff(user);
//        }
    }

    //
    // Serial stuff
    //
    @Override
    public void onInitSuccess(SerialPort serialPort) {
        System.out.println("COM port found: " + serialPort.getDescriptivePortName());
        serMgr().initComPort(serialPort);
        serMgr().sendInit(Main.DOOR_COUNT);
    }

    @Override
    public void onSerialError(String error) {
        // TODO show dialog and exit(-1) ??
        System.err.println(error);
        JOptionPane.showMessageDialog(
                Application.this,
                error,
                "OpenWhileLocked",
                JOptionPane.ERROR_MESSAGE
        );
        if (!Main.DEBUG_MODE) {
            Main.shutDown();
        }
    }

    @Override
    public void onNewMessage(SerialMessage message) {
        if (message.getCommand().contains("D")) {
            Door door = doorMgr().updateDoor(message);
            if (door != null) {
                doorsPanel.updateDoor(door);
                inventoryPanel.updateComponents();
            }

            SwingUtilities.invokeLater(() -> {
                doDoorLogic(usrMgr().getSelectedUser());
            });
        }

    }

    //
    // Web stuff
    //
    @Override
    public void onFinishedRequest(String methodName, Vector response) {
        switch (methodName) {
            case WebCall_Register:
                webRegistered();
                break;

            case WebCall_UnRegister:
                break;

            case WebCall_LogOn:
                cardLayout.show(mainPanel, VIEW_INVENTORY);
                userPanel.updateComponents(usrMgr().getSelectedUser());
                doorMgr().unlockDoors();
                serMgr().sendUnlockAll();
                usrMgr().startTimer(newTime -> userPanel.updateTimer(newTime));
                break;

            case WebCall_LogOff:
                cardLayout.show(mainPanel, VIEW_MAIN);
                usrMgr().logOffUser();
                doorMgr().lockDoors();
                serMgr().sendLockAll();
                userPanel.updateComponents(usrMgr().getSelectedUser());
                doDoorLogic(usrMgr().getSelectedUser());
                break;

            case WebCall_GetUsers:
                webReceivedUsers(response);
                break;
        }
        webMgr().setWebSuccess(true);
    }

    @Override
    public void onFailedRequest(String methodName, Exception ex, int fault) {
        System.err.println("Web call error for: " + methodName + " -> " + ex);
        webMgr().setWebSuccess(false);
        JOptionPane.showMessageDialog(
                Application.this,
                "Web call error for: " + methodName + " -> " + ex,
                "Web call error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
