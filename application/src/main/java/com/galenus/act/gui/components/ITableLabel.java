package com.galenus.act.gui.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ITableLabel extends ILabel {
    private final ILabel textLabel;
    private boolean hasIcon;

    public ITableLabel(Color background) {
        this(background, 0, true, (ImageIcon)null, "");
    }

    public ITableLabel(Color background, int row, boolean isSelected, ImageIcon icon) {
        this(background, row, isSelected, icon, "");
    }

    public ITableLabel(Color background, int row, boolean isSelected, String txt) {
        super(txt);
        this.textLabel = new ILabel("");
        this.hasIcon = true;
        this.updateBackground(background, row, isSelected);
        this.hasIcon = false;
        super.setOpaque(true);
    }

    public ITableLabel(Color background, int row, boolean isSelected, ImageIcon icon, String txt) {
        super(icon);
        this.textLabel = new ILabel("");
        this.hasIcon = true;
        this.createTextLabel(txt);
        this.updateBackground(background, row, isSelected);
        super.setOpaque(true);
        super.setLayout(new GridBagLayout());
        super.add(this.textLabel);
    }

    private void createTextLabel(String txt) {
        this.textLabel.setText(txt);
        this.textLabel.setForeground(Color.WHITE);
        Font f = this.textLabel.getFont();
        this.textLabel.setFont(new Font(f.getName(), 1, f.getSize() - 5));
        this.textLabel.setOpaque(false);
    }

    public void updateWithTableComponent(Component c, int row, boolean isSelected) {
        this.updateBackground(c.getBackground(), row, isSelected);
        this.updateForeground(c.getForeground());
    }

    public void updateBorder(Border border) {
        super.setBorder(border);
    }

    public void updateForeground(Color foreground) {
        super.setForeground(foreground);
    }

    public void updateBackground(Color background, int row, boolean isSelected) {
        if (row % 2 != 1 && !isSelected) {
            super.setBackground(Color.WHITE);
        } else {
            super.setBackground(background);
        }

    }

    public void setText(String text) {
        if (this.hasIcon) {
            this.textLabel.setText(text);
        } else {
            super.setText(text);
        }

    }
}
