package com.galenus.act.gui.components;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.GuiInterface;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class IUserTile extends JPanel implements GuiInterface, /*ActionListener,*/ MouseListener {

    public interface OnTileClickListener {
        void onTileClick(MouseEvent e, IUserTile tile);
    }

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JButton iconBtn;
    private JTextPane nameTp;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private User user;
    private OnTileClickListener clickListener;

    private boolean isSelected = false;
    private Color background;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public IUserTile(User user) {
        this.user = user;

        this.addMouseListener(this);
        this.background = getBackground();

        initializeComponents();
        initializeLayouts();
        updateComponents();
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void addOnTileClickListener(OnTileClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public User getUser() {
        return user;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            this.setBackground(Color.gray);
        } else {
            this.setBackground(background);
        }
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        iconBtn = new JButton();
        iconBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        //iconBtn.addActionListener(this);
        iconBtn.addMouseListener(this);

        nameTp = new JTextPane();

        StyledDocument doc = nameTp.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        nameTp.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameTp.setAlignmentY(Component.CENTER_ALIGNMENT);
        nameTp.setFocusable(false);
        nameTp.setOpaque(false);
        nameTp.setBackground(new Color(0,0,0,0));
        nameTp.setBorder(null);
        nameTp.setEditable(false);
        nameTp.addMouseListener(this);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(iconBtn);
        add(nameTp);

        setPreferredSize(new Dimension(128, 128));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    @Override
    public void updateComponents(Object... args) {
        iconBtn.setIcon(user.getAvatar());
        nameTp.setText(user.getFirstName());
    }

    //
    // Mouse listener
    //
    @Override
    public void mouseClicked(MouseEvent e) {
        if (clickListener != null) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                clickListener.onTileClick(e, this);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(Color.gray.brighter());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setSelected(isSelected);
    }

}
