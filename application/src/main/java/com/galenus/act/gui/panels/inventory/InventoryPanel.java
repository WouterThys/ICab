package com.galenus.act.gui.panels.inventory;

import com.galenus.act.classes.Door;

import javax.swing.event.ListSelectionEvent;

public class InventoryPanel extends InventoryPanelLayout {


    public InventoryPanel() {
        initializeComponents();
        initializeLayouts();
        updateComponents();
    }


    //
    // Table value selected
    //
    @Override
    public void valueChanged(ListSelectionEvent e) {
        Door newDoor = doorTable.getSelectedItem();
        if (selectedDoor == null || !selectedDoor.equals(newDoor)) {
            selectedDoor = newDoor;
            updateItemTable(selectedDoor.getItemList());
        }
    }
}
