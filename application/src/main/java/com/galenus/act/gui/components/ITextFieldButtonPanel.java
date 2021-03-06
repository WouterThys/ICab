package com.galenus.act.gui.components;

import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.interfaces.IEditedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ITextFieldButtonPanel extends JPanel implements GuiInterface {

    protected String hint;
    protected String text;
    protected ImageIcon buttonIcon;

    protected IEditedListener editedListener;
    protected String fieldName;

    protected ITextField textField;
    protected JButton button;
    protected ActionListener actionListener;

    public ITextFieldButtonPanel(String hint, ImageIcon buttonIcon) {
        this(hint, "", null, buttonIcon, null);
    }

    public ITextFieldButtonPanel(String hint, ImageIcon buttonIcon, ActionListener actionListener) {
        this(hint, "", null, buttonIcon, actionListener);
    }

    public ITextFieldButtonPanel(String hint, String fieldName, IEditedListener editedListener, ImageIcon buttonIcon, ActionListener actionListener) {
        super();

        this.text = "";
        this.hint = hint;
        this.fieldName = fieldName;
        this.editedListener = editedListener;
        this.buttonIcon = buttonIcon;
        this.actionListener = actionListener;

        initializeComponents();
        initializeLayouts();
        updateComponents();
    }

    public ITextFieldButtonPanel(String hint, String fieldName, IEditedListener editedListener, ImageIcon buttonIcon) {
        this(hint, fieldName, editedListener, buttonIcon, null);
    }

    public void addFieldEditedListener(IEditedListener listener, String fieldName) {
        textField.addEditedListener(listener, fieldName);
    }

    public void addButtonActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        button.addActionListener(actionListener);
    }

    @Override
    public void initializeComponents() {
        // Text field
        textField = new ITextField(hint);
        if (editedListener != null) {
            textField.addEditedListener(editedListener, fieldName);
        }

        // Button
        button = new JButton();
        if (buttonIcon != null) {
            button.setIcon(buttonIcon);
        }
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
    }

    @Override
    public void updateComponents(Object... object) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setTextEnabled(enabled);
        setButtonEnabled(enabled);
    }

    public void setTextEnabled(boolean enabled) {
        textField.setEnabled(enabled);
    }

    public void setButtonEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    public void setText(String text) {
        this.text = text;
        textField.setText(text);
    }

    public void clearText() {
        this.text = "";
        textField.clearText();
    }

    public String getText() {
        this.text = textField.getText();
        return text;
    }

    public JButton getButton() {
        return button;
    }
}
