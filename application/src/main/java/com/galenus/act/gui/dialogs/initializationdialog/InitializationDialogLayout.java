package com.galenus.act.gui.dialogs.initializationdialog;

import com.galenus.act.Application;
import com.galenus.act.gui.components.IDialog;
import com.galenus.act.gui.components.ILabel;
import com.galenus.act.classes.interfaces.SerialListener;
import com.galenus.act.classes.interfaces.WebCallListener;

import javax.swing.*;
import java.awt.*;

abstract class InitializationDialogLayout extends IDialog {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
   private JProgressBar progressBar;
   private ILabel messageLbl;

   /*
    *                  VARIABLES
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    SerialListener serialListener;
    WebCallListener webCallListener;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
   InitializationDialogLayout(Application application, String title, SerialListener serialListener, WebCallListener webCallListener) {
       super(application, title);

       this.serialListener = serialListener;
       this.webCallListener = webCallListener;
   }


    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    void setProgress(int progress) {
        progressBar.setValue(progress);
    }

    void setMessage(String message) {
        messageLbl.setText(message);
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        // Dialog
        showTitlePanel(false);
        getButtonOK().setVisible(false);

        // Components
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressBar.setPreferredSize(new Dimension(150, 20));
        progressBar.setValue(30);

        messageLbl = new ILabel("Label");
    }

    @Override
    public void initializeLayouts() {
        getContentPanel().setLayout(new BoxLayout(getContentPanel(), BoxLayout.Y_AXIS));
        getContentPanel().add(messageLbl);
        getContentPanel().add(progressBar);
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        pack();
    }

    @Override
    public void updateComponents(Object... args) {

    }
}
