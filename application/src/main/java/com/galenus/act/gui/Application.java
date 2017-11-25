package com.galenus.act.gui;

import com.galenus.act.classes.User;
import com.galenus.act.classes.managers.UserManager;
import com.galenus.act.gui.components.IUserTile;
import com.galenus.act.gui.panels.logon.LogOnPanel;
import com.galenus.act.utils.SoapUtils;
import com.galenus.act.utils.resources.ImageResource;
import com.galenus.act.web.OnWebCallListener;
import org.ksoap2.serialization.SoapObject;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static com.galenus.act.classes.managers.UserManager.usrMgr;
import static com.galenus.act.web.WebManager.*;

public class Application extends JFrame implements GuiInterface, OnWebCallListener {

    public static String startUpPath;
    public static ImageResource imageResource;

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private LogOnPanel logOnPanel;

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
        // Start register
        webMgr().registerDevice();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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
