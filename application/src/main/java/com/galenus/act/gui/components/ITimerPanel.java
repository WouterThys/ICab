package com.galenus.act.gui.components;

import com.galenus.act.classes.interfaces.GuiInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.galenus.act.Application.imageResource;
import static com.galenus.act.classes.managers.UserManager.usrMgr;
import static com.galenus.act.classes.managers.web.WebManager.webMgr;

public class ITimerPanel extends JPanel implements GuiInterface {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private ILabel timerIconLbl;
    private ILabel timeLbl;

    private AbstractAction stopTimerAction;
    private AbstractAction startTimerAction;
    private AbstractAction lockDoorsAction;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public ITimerPanel() {
        initializeComponents();
        initializeLayouts();
        updateComponents();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void updateEnabledComponents() {
        boolean running = usrMgr().isTimerRunning();
        boolean canStopTimer = usrMgr().getSelectedUser() != null && usrMgr().getSelectedUser().isCanStopTimer();

        stopTimerAction.setEnabled(canStopTimer && running);
        startTimerAction.setEnabled(canStopTimer && !running);
    }


    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        timerIconLbl = new ILabel(imageResource.readImage("Timer.Timer"));
        timerIconLbl.setPreferredSize(new Dimension(256,256));
        timerIconLbl.setMinimumSize(new Dimension(256,256));
        timerIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        timerIconLbl.setVerticalAlignment(SwingConstants.CENTER);

        timeLbl = new ILabel("02:00", ILabel.CENTER);
        timeLbl.setFont(100, Font.BOLD);

        startTimerAction = new AbstractAction("Start", imageResource.readImage("Timer.Start")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    usrMgr().restartTimer();
                    webMgr().startedTimer(usrMgr().getSelectedUser());
                    updateEnabledComponents();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        stopTimerAction = new AbstractAction("Pause", imageResource.readImage("Timer.Stop")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    usrMgr().stopTimer();
                    webMgr().stoppedTimer(usrMgr().getSelectedUser());
                    updateEnabledComponents();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        lockDoorsAction = new AbstractAction("Lock", imageResource.readImage("Timer.Lock")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    usrMgr().stopUser();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    private JPanel createTimerPanel() {
        JPanel timerPanel = new JPanel(new BorderLayout());

        timerPanel.add(timerIconLbl, BorderLayout.CENTER);
        timerPanel.add(timeLbl, BorderLayout.SOUTH);

        //timerPanel.setPreferredSize(new Dimension(300, 600));

        return timerPanel;
    }

    private JPanel createActionPanel() {
        JPanel actionPnl = new JPanel(new BorderLayout());

        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));

        toolBar.add(lockDoorsAction);
        toolBar.add(stopTimerAction);
        toolBar.add(startTimerAction);

        actionPnl.add(toolBar, BorderLayout.CENTER);

        return actionPnl;
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        add(createTimerPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);

        updateEnabledComponents();
    }

    @Override
    public void updateComponents(Object... args) {
        if (args.length > 0 && args[0] != null) {
            String newTime = args[0].toString();
            if (newTime != null && !newTime.isEmpty()) {
                if (newTime.contains("-")) {
                    timeLbl.setForeground(Color.RED);
                } else {
                    timeLbl.setForeground(Color.BLACK);
                }
                timeLbl.setText(args[0].toString());
            }
        }
    }
}
