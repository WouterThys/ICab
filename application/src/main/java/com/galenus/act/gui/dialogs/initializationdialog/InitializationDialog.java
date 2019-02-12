package com.galenus.act.gui.dialogs.initializationdialog;

import com.galenus.act.Application;
import com.galenus.act.classes.interfaces.SerialListener;
import com.galenus.act.classes.interfaces.WebCallListener;
import com.galenus.act.classes.managers.serial.SerialManager;

import javax.swing.*;

import static com.galenus.act.classes.managers.serial.SerialManager.serMgr;
import static com.galenus.act.classes.managers.web.WebManager.webMgr;
import static com.galenus.act.utils.resources.Settings.getSettings;

public class InitializationDialog extends InitializationDialogLayout {

    private SerialManager.FindComPortThread serialInitWorker;

    public InitializationDialog(Application application, String title, SerialListener serialListener, WebCallListener webCallListener) {
        super(application, title, serialListener, webCallListener);

        initializeComponents();
        initializeLayouts();
        updateComponents();

        SwingUtilities.invokeLater(this::one_initDoors);
    }

    private void one_initDoors() {
        two_initSerial();
    }

    private void two_initSerial() {
        serMgr().init(serialListener);
        serMgr().registerShutDownHook();

        serialInitWorker = new SerialManager.FindComPortThread(serialListener);

        setMessage("Searching COM ports");

        serialInitWorker.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("state")) {
                if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                    three_initWebService();
                }
            } else if (evt.getPropertyName().equals("progress")) {
                int progress = (Integer) evt.getNewValue();
                setProgress(progress);
            }
        });
        serialInitWorker.execute();
    }

    private void three_initWebService() {
        webMgr().init( application,
                getSettings().getName(),
                getSettings().getWebUrl(),
                "http://tempuri.org/",
                60000);
        webMgr().registerShutDownHook();
        webMgr().addOnWebCallListener(webCallListener);
        webMgr().registerDevice();
        super.onOK();
    }

    @Override
    protected void onCancel() {
        // Stop int
        super.onCancel();
    }
}
