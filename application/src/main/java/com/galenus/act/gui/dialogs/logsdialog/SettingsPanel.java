package com.galenus.act.gui.dialogs.logsdialog;

import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.gui.components.ICheckBox;
import com.galenus.act.gui.components.ISpinner;
import com.galenus.act.gui.components.ITextField;
import com.galenus.act.utils.GuiUtils;

import javax.swing.*;

import java.awt.*;

import static com.galenus.act.utils.resources.Settings.getSettings;

public class SettingsPanel extends JPanel implements GuiInterface {
    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private ICheckBox isDebugModeCb;
    private ICheckBox isFullScreenCb;
    private ISpinner doorCountSp;
    private ISpinner logonTimeSp;
    private ISpinner pingDelaySp;
    private ISpinner tabFontSizeSp;
    private ITextField webUrlTf;
    private ITextField nameTf;


    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public SettingsPanel() {
        super();

        initializeComponents();
        initializeLayouts();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    void updateEnabledComponents() {

    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    //
    // Gui
    //
    @Override
    public void initializeComponents() {
        isDebugModeCb = new ICheckBox(false);
        isFullScreenCb = new ICheckBox(false);

        doorCountSp = new ISpinner(false, new SpinnerNumberModel(1,1,10,1));
        logonTimeSp = new ISpinner(false, new SpinnerNumberModel(10,10,100,5));
        pingDelaySp = new ISpinner(false, new SpinnerNumberModel(5,5,100,1));
        tabFontSizeSp = new ISpinner(false, new SpinnerNumberModel(10, 10, 100, 1));
        webUrlTf = new ITextField(false);
        nameTf = new ITextField(false);
    }

    @Override
    public void initializeLayouts() {
        JPanel panel = new JPanel(new GridBagLayout());
        GuiUtils.GridBagHelper gbc = new GuiUtils.GridBagHelper(panel);
        gbc.addLine("Web URL: ", webUrlTf);
        gbc.addLine("Name: ", nameTf);
        gbc.addLine("Debug mode: ", isDebugModeCb);
        gbc.addLine("Full screen: ", isFullScreenCb);
        gbc.addLine("Door count: ", doorCountSp);
        gbc.addLine("Logon time (s): ", logonTimeSp);
        gbc.addLine("Ping delay (s): ", pingDelaySp);
        gbc.addLine("Font size: ", tabFontSizeSp);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(panel, BorderLayout.NORTH);
    }

    @Override
    public void updateComponents(Object... args) {
        isDebugModeCb.setSelected(getSettings().isDebugMode());
        isFullScreenCb.setSelected(getSettings().isFullScreen());
        doorCountSp.setTheValue(getSettings().getDoorCount());
        logonTimeSp.setTheValue(getSettings().getUserLogonTime());
        pingDelaySp.setTheValue(getSettings().getPingDelay() / 1000);
        tabFontSizeSp.setTheValue(getSettings().getTabFontSize());
        webUrlTf.setText(getSettings().getWebUrl());
        nameTf.setText(getSettings().getName());
    }
}