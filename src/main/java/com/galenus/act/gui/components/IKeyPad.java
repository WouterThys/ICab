package com.galenus.act.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class IKeyPad extends JPanel {

    private int maxDigits = 10;
    private final JTextArea text = new JTextArea(1, maxDigits);
    private final JButton clear = new JButton(new Clear("Clear"));
    private final JButton enter = new JButton(new Enter("Enter"));
    private final List<NumberButton> numbers = new ArrayList<NumberButton>();

    /**
     * Construct a numeric key pad that accepts up to ten digits.
     */
    public IKeyPad() {
        super(new BorderLayout());

        JPanel display = new JPanel();
        text.setFont(new Font("Dialog", Font.PLAIN, 24));
        text.setEditable(false);
        text.setFocusable(false);
        display.add(text);
        this.add(display, BorderLayout.NORTH);

        JPanel pad = new JPanel(new GridLayout(4, 3));
        for (int i = 0; i < 10; i++) {
            NumberButton n = new NumberButton(i);
            numbers.add(n);
            if (i > 0) {
                pad.add(n);
            }
        }
        pad.add(clear);
        clear.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_CLEAR, 0), clear.getText());
        this.getActionMap().put(clear.getText(), new Click(clear));
        pad.add(numbers.get(0));
        pad.add(enter);
        enter.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0), enter.getText());
        this.getActionMap().put(enter.getText(), new Click(enter));
        this.add(pad, BorderLayout.CENTER);
    }

    /**
     * Construct a numeric key pad that accepts up to <code>maxDigits<code>.
     */
    public IKeyPad(int maxDigits) {
        this();
        this.maxDigits = maxDigits;
        this.text.setColumns(maxDigits);
    }

    private class Clear extends AbstractAction {

        public Clear(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            text.setText("");
            for (NumberButton n : numbers) {
                n.setEnabled(true);
            }
        }
    }

    private class Enter extends AbstractAction {

        public Enter(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Entered: " + text.getText());
            clear.getAction().actionPerformed(e);
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
     * <code>text<code>, accepting no more than <code>maxDigits<code>.
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
                    if (text.getText().length() < maxDigits) {
                        text.append(cmd);
                    }
                    if (text.getText().length() == maxDigits) {
                        for (NumberButton n : numbers) {
                            n.setEnabled(false);
                        }
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