package com.galenus.act.gui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static com.galenus.act.gui.Application.imageResource;

public class ILabel extends JLabel {

    public ILabel() {
        super();
    }

    public ILabel(Icon image) {
        super(image);
    }

    public ILabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public ILabel(String text) {
        super(text);
    }

    public ILabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public ILabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }


    public void setFontSize(int size) {
        Font labelFont = this.getFont();
        this.setFont(new Font(labelFont.getName(), labelFont.getStyle(), size));
    }

    public void setFont(int size, int style) {
        Font labelFont = this.getFont();
        this.setFont(new Font(labelFont.getName(), style, size));
    }

    public void setFont(int style) {
        Font labelFont = this.getFont();
        this.setFont(new Font(labelFont.getName(), style, labelFont.getSize()));
    }

    public void setIcon(String key) {
        try {
            setIcon(imageResource.readImage(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adjustTextSize() {
        Font labelFont = this.getFont();
        String labelText = this.getText();

        int stringWidth = this.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = this.getWidth();

        double widthRatio = (double) componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = this.getHeight();

        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        this.setFont(new Font(labelFont.getName(), labelFont.getStyle(), fontSizeToUse));
    }

    public static VerticalLabelUI createVerticalLabel(boolean clockwise) {
        return new VerticalLabelUI(clockwise);
    }


    private static class VerticalLabelUI extends BasicLabelUI {
        static {
            labelUI = new VerticalLabelUI(false);
        }

        protected boolean clockwise;


        public VerticalLabelUI(boolean clockwise) {
            super();
            this.clockwise = clockwise;
        }


        public Dimension getPreferredSize(JComponent c) {
            Dimension dim = super.getPreferredSize(c);
            return new Dimension( dim.height, dim.width );
        }

        private static Rectangle paintIconR = new Rectangle();
        private static Rectangle paintTextR = new Rectangle();
        private static Rectangle paintViewR = new Rectangle();
        private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

        public void paint(Graphics g, JComponent c) {

            JLabel label = (JLabel)c;
            String text = label.getText();
            Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

            if ((icon == null) && (text == null)) {
                return;
            }

            FontMetrics fm = g.getFontMetrics();
            paintViewInsets = c.getInsets(paintViewInsets);

            paintViewR.x = paintViewInsets.left;
            paintViewR.y = paintViewInsets.top;

            // Use inverted height & width
            paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
            paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);

            paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
            paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

            String clippedText =
                    layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);

            Graphics2D g2 = (Graphics2D) g;
            AffineTransform tr = g2.getTransform();
            if (clockwise) {
                g2.rotate( Math.PI / 2 );
                g2.translate( 0, - c.getWidth() );
            } else {
                g2.rotate( - Math.PI / 2 );
                g2.translate( - c.getHeight(), 0 );
            }

            if (icon != null) {
                icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
            }

            if (text != null) {
                int textX = paintTextR.x;
                int textY = paintTextR.y + fm.getAscent();

                if (label.isEnabled()) {
                    paintEnabledText(label, g, clippedText, textX, textY);
                } else {
                    paintDisabledText(label, g, clippedText, textX, textY);
                }
            }

            g2.setTransform( tr );
        }
    }
}
