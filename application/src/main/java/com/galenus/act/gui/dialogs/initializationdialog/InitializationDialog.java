package com.galenus.act.gui.dialogs.initializationdialog;

import com.galenus.act.gui.Application;
import com.galenus.act.serial.SerialListener;
import com.galenus.act.serial.SerialManager;
import com.galenus.act.web.WebCallListener;

import javax.swing.*;

import static com.galenus.act.serial.SerialManager.serMgr;
import static com.galenus.act.web.WebManager.webMgr;

public class InitializationDialog extends InitializationDialogLayout {

    SerialManager.FindComPortThread serialInitWorker;

    public InitializationDialog(Application application, String title, SerialListener serialListener, WebCallListener webCallListener) {
        super(application, title, serialListener, webCallListener);

        initializeComponents();
        initializeLayouts();
        updateComponents();

        SwingUtilities.invokeLater(this::one_initSerial);
    }

    private void one_initSerial() {
        serMgr().init(serialListener);
        serMgr().registerShutDownHook();

        serialInitWorker = new SerialManager.FindComPortThread(serialListener);

        setMessage("Searching COM ports");

        serialInitWorker.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("state")) {
                if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                    two_initWebService();
                }
            } else if (evt.getPropertyName().equals("progress")) {
                int progress = (Integer) evt.getNewValue();
                setProgress(progress);
            }
        });
        serialInitWorker.execute();
    }

    private void two_initWebService() {
        webMgr().init( application,
                "ICAB",
                "http://sp0007test/juliette/oriswsmattteo.asmx",
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
