package com.galenus.act.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static com.galenus.act.Application.imageResource;

public class IKeyPad extends JPanel {

    public interface KeyPadListener {
        void onDigitsEntered(String entered);
    }

    private int maxDigits = 4;
    private final JLabel pinField = new JLabel("", SwingConstants.CENTER);
    private final List<NumberButton> numbers = new ArrayList<NumberButton>();

    private String password = "";
    private KeyPadListener keyPadListener;

    /**
     * Construct a numeric key pad that accepts up to ten digits.
     */
    public IKeyPad() {
        super(new BorderLayout());

        JPanel display = new JPanel();
        pinField.setFont(new Font("Dialog", Font.PLAIN, 60));
        pinField.setFocusable(false);
        pinField.setOpaque(true);
        pinField.setBackground(Color.WHITE);
        pinField.setPreferredSize(new Dimension(200, 80));

        display.add(pinField);
        this.add(display, BorderLayout.NORTH);

        JPanel pad = new JPanel(new GridLayout(4, 3));
        for (int i = 0; i < 10; i++) {
            NumberButton n = new NumberButton(i);
            numbers.add(n);
            if (i > 0) {
                pad.add(n);
            }
        }
        JButton clearBtn = new JButton(new Clear("", imageResource.readImage("KeyPad.Clear")));
        pad.add(clearBtn);
        clearBtn.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_CLEAR, 0), clearBtn.getText());
        this.getActionMap().put(clearBtn.getText(), new Click(clearBtn));
        pad.add(numbers.get(0));
        JButton enterBtn = new JButton(new Enter("", imageResource.readImage("KeyPad.Enter")));
        pad.add(enterBtn);
        enterBtn.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enterBtn.getText());
        this.getActionMap().put(enterBtn.getText(), new Click(enterBtn));
        this.add(pad, BorderLayout.CENTER);
    }

    public void addKeyPadListener(KeyPadListener keyPadListener) {
        this.keyPadListener = keyPadListener;
    }

    public void setBackgroundColor(Color color) {
        pinField.setBackground(color);
    }

    public void clear() {
        pinField.setText("");
        pinField.setBackground(Color.white);
        password = "";
        for (NumberButton n : numbers) {
            n.setEnabled(true);
        }
    }

    private void enter() {
        if (keyPadListener != null) {
            keyPadListener.onDigitsEntered(password);
        }
    }

    private class Clear extends AbstractAction {

        public Clear(String name) {
            super(name);
        }

        public Clear(String name, Icon icon) {
            super(name, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            clear();
        }
    }

    private class Enter extends AbstractAction {

        public Enter(String name) {
            super(name);
        }

        public Enter(String name, Icon icon) {
            super(name, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            enter();
        }
    }

    private class Click extends AbstractAction {

        JButton button;

        public Click(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            button.doClick();
        }
    }

    /*
     * A numeric button with digit key bindings that appends to
     * <code>pinField<code>, accepting no more than <code>maxDigits<code>.
     */
    private class NumberButton extends JButton {

        public NumberButton(int number) {
            super(String.valueOf(number));
            this.setFocusable(false);
            Font f = this.getFont();
            this.setFont(new Font(f.getName(), Font.BOLD, 30));
            this.setAction(new AbstractAction(this.getText()) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String cmd = e.getActionCommand();
                    if (pinField.getText().length() < maxDigits) {
                        password += cmd;

                        StringBuilder newTxt = new StringBuilder();
                        for (int i = 0; i < password.length(); i++) {
                            newTxt.append("*");
                        }
                        //newTxt.append(password.substring(password.length()-1));
                        pinField.setText(newTxt.toString());
                    }
                    if (pinField.getText().length() == maxDigits) {
                        for (NumberButton n : numbers) {
                            n.setEnabled(false);
                        }
                        enter();
                    }
                }
            });
            IKeyPad.this.getInputMap().put(KeyStroke.getKeyStroke(
                    KeyEvent.VK_0 + number, 0), this.getText());
            IKeyPad.this.getInputMap().put(KeyStroke.getKeyStroke(
                    KeyEvent.VK_NUMPAD0 + number, 0), this.getText());
            IKeyPad.this.getActionMap().put(this.getText(), new Click(this));
        }
    }
}