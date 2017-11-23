package com.galenus.act.gui;

import com.galenus.act.web.OnWebCallListener;

import javax.swing.*;
import java.util.Vector;

import static com.galenus.act.web.WebManager.*;

public class Application extends JFrame implements GuiInterface, OnWebCallListener {


    public Application() {
        // Add web call listener
        webMgr().addOnWebCallListener(this);
        // Init gui
        initializeComponents();
        initializeLayouts();
        // Start register
        webMgr().registerDevice();
    }

    //
    // Gui stuff
    //
    @Override
    public void initializeComponents() {

    }

    @Override
    public void initializeLayouts() {

    }

    @Override
    public void updateComponents(Object... args) {

    }

    //
    // Web stuff
    //
    @Override
    public void onFinishedRequest(String methodName, Vector response) {
        switch (methodName) {
            case WebCall_DeviceRegister:
                // Fetch users
                webMgr().getDeviceUsers();
                break;
            case WebCall_DeviceUnRegister:
                break;
            case WebCall_DeviceGetUsers:
                // Create user list

                // Update components
                updateComponents();
                break;
        }
    }

    @Override
    public void onFailedRequest(String methodName, Exception ex, int fault) {
        System.err.println("Web call error for: " + methodName + " -> " + ex);
    }
}
