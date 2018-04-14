package com.galenus.act.gui.components;

import com.galenus.act.classes.interfaces.GuiInterface;

import javax.swing.*;
import java.awt.*;

import static com.galenus.act.Application.imageResource;

public class ITimerPanel extends JPanel implements GuiInterface {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private ILabel timerIconLbl;
    private ILabel timeLbl;

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



    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        timerIconLbl = new ILabel(imageResource.readImage("Timer.Timer"));
        timerIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        timerIconLbl.setVerticalAlignment(SwingConstants.CENTER);

        timeLbl = new ILabel("02:00", ILabel.CENTER);
        timeLbl.setFont(60, Font.BOLD);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        add(timerIconLbl, BorderLayout.CENTER);
        add(timeLbl, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(300, 400));
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
