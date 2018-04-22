package com.galenus.act;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

import static com.galenus.act.Application.settings;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();

            Application app = new Application();

            app.setPreferredSize(new Dimension(1500, 800));
            if (settings.isFullScreen()) {
                app.setUndecorated(true);
            }
            app.setTitle("I-CAB");
            app.setLocationByPlatform(true);
            app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            app.pack();
            app.setVisible(true);
            app.start();
        });
    }

    static void shutDown() {
        System.exit(-1);
    }

    private static void setLookAndFeel() {

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel() {
                @Override
                public UIDefaults getDefaults() {
                    UIDefaults defaults = super.getDefaults();

                    defaults.put("defaultFont", new Font(Font.SANS_SERIF, Font.PLAIN, 15));
                    defaults.put("ProgressBar.background", Color.YELLOW);
                    defaults.put("ProgressBar.foreground", Color.BLUE);
                    defaults.put("ProgressBar.selectionBackground", Color.RED);
                    defaults.put("ProgressBar.selectionForeground", Color.GREEN);
                    return defaults;
                }
            });
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

//        UIManager.put("ProgressBar.background", Color.YELLOW);
//        UIManager.put("ProgressBar.foreground", Color.BLUE);
//        UIManager.put("ProgressBar.selectionBackground", Color.RED);
//        UIManager.put("ProgressBar.selectionForeground", Color.GREEN);

    }

    public static void closeApplication(int status) {
        System.exit(status);
    }

}
