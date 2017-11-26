package com.galenus.act.gui;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.classes.User;
import com.galenus.act.gui.dialogs.seriallogsdialog.SerialLogsDialog;
import com.galenus.act.gui.panels.logon.LogOnPanel;
import com.galenus.act.serial.SerialListener;
import com.galenus.act.serial.SerialManager;
import com.galenus.act.serial.SerialMessage;
import com.galenus.act.utils.resources.ImageResource;
import com.galenus.act.web.WebCallListener;
import org.ksoap2.serialization.SoapObject;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static com.galenus.act.classes.managers.UserManager.usrMgr;
import static com.galenus.act.serial.SerialManager.serMgr;
import static com.galenus.act.web.WebManager.*;

public class Application extends JFrame implements GuiInterface, SerialListener, WebCallListener {

    public static String startUpPath;
    public static ImageResource imageResource;

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private LogOnPanel logOnPanel;

    private ProgressMonitor monitor;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public Application(String startUpPath) {
        Application.startUpPath = startUpPath;
        Application.imageResource = new ImageResource("", "icons.properties");

        // Add web call listener
        webMgr().addOnWebCallListener(this);

        // Init gui
        initializeComponents();
        initializeLayouts();

        // Start initialize: init serial -> ok = init web -> ok = get users
        initializeSerial();

        // Start register
        //webMgr().registerDevice();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void startWait() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void stopWait() {
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void initializeWebService() {
        webMgr().init(
                "ICAB",
                "http://sp0007test/juliette/oriswsmattteo.asmx",
                "http://tempuri.org/",
                60000);
        webMgr().registerShutDownHook();
    }

    private void initializeSerial() {
        serMgr().init(this);
        serMgr().registerShutDownHook();

        SerialManager.FindComPortThread worker = new SerialManager.FindComPortThread(this);

        initProgressMonitor("Find COM ports", "Searching ");
        startWait();
        worker.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("state")) {
                if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                    stopWait();
                }
            } else if (evt.getPropertyName().equals("progress")) {
                if (monitor != null) {
                    int progress = (Integer) evt.getNewValue();
                    String note = monitor.getNote();
                    note += ".";
                    if (note.length() > 50) {
                        note = ".";
                    }
                    monitor.setProgress(progress);
                    monitor.setNote(note);
                    if (monitor.isCanceled() || worker.isDone()) {
                        if (monitor.isCanceled()) {
                            worker.cancel(true);
                        }
                    }
                }
            }
        });
        worker.execute();
    }

    private void initProgressMonitor(String message, String note) {
        monitor = new ProgressMonitor(this, message, note, 0, 100);
        monitor.setProgress(0);
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

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //
    // Gui stuff
    //
    @Override
    public void initializeComponents() {
        logOnPanel = new LogOnPanel();

        // Menu panel
        JMenuBar menuBar = new JMenuBar();
        JMenu serialMenu = new JMenu("Serial");
        JMenuItem serialLogs = new JMenuItem("Serial logs");
        serialLogs.addActionListener(e -> {
            SerialLogsDialog dialog = new SerialLogsDialog(this, "Serial logs", serMgr().getSerialPort());
            dialog.showDialog();
        });

        serialMenu.add(serialLogs);
        menuBar.add(serialMenu);

        this.setJMenuBar(menuBar);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        add(logOnPanel, BorderLayout.CENTER);

        pack();
    }

    @Override
    public void updateComponents(Object... args) {
        // Users
        logOnPanel.setUsers(usrMgr().getUserList());

        // Other

        pack();

    }

    //
    // Serial stuff
    //
    @Override
    public void onInitSuccess(SerialPort serialPort) {
        System.out.println("COM port found: " + serialPort.getDescriptivePortName());
        serMgr().initComPort(serialPort);
        serMgr().sendLockAll();
    }

    @Override
    public void onSerialError(String error) {
        // TODO show dialog and exit(-1) ??
        System.err.println(error);
    }

    @Override
    public void onNewMessage(SerialMessage message) {
        System.out.println("New message from " + message.getSender() + ": " + message.getCommand() + "->" + message.getMessage());
    }

    //
    // Web stuff
    //
    @Override
    public void onFinishedRequest(String methodName, Vector response) {
        switch (methodName) {
            case WebCall_DeviceRegister:
                webRegistered();
                break;
            case WebCall_DeviceUnRegister:
                break;
            case WebCall_DeviceGetUsers:
                webReceivedUsers(response);
                break;
        }
    }

    @Override
    public void onFailedRequest(String methodName, Exception ex, int fault) {
        System.err.println("Web call error for: " + methodName + " -> " + ex);
    }
}
