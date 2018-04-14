package com.galenus.act.gui.panels.inventory;

import com.galenus.act.classes.Door;
import com.galenus.act.classes.Item;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.managers.DoorManager;
import com.galenus.act.gui.components.ITable;
import com.galenus.act.gui.models.DoorTableModel;
import com.galenus.act.gui.models.ItemTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.galenus.act.classes.managers.DoorManager.doorMgr;

abstract class InventoryPanelLayout extends JPanel implements GuiInterface, ListSelectionListener {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private DoorTableModel doorTableModel;
    ITable<Door> doorTable;

    private ItemTableModel itemTableModel;
    ITable<Item> itemTable;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    Door selectedDoor;

    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    InventoryPanelLayout() {
        super(new BorderLayout());
    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    void updateItemTable(List<Item> itemList) {
        if (itemList != null) {
            itemTableModel.setItemList(itemList);
        }
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        // Doors
        doorTableModel = new DoorTableModel(doorMgr().getDoorList());
        doorTable = new ITable<>(doorTableModel);
        doorTable.setRowHeight(36);
        doorTable.setExactColumnWidth(0, 36);
        doorTable.getSelectionModel().addListSelectionListener(this);

        // Items
        itemTableModel = new ItemTableModel(new ArrayList<>());
        itemTable = new ITable<>(itemTableModel);
        itemTable.setDefaultRenderer(Object.class, new ItemTableModel.ItemTableRenderer());
        itemTable.setExactColumnWidth(0, 36);
        itemTable.setEnabled(false);
        //itemTable.getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void initializeLayouts() {
        JScrollPane doorScrollPane = new JScrollPane(doorTable);
        doorScrollPane.setPreferredSize(new Dimension(200, 400));
        JScrollPane itemScrollPane = new JScrollPane(itemTable);

        add(doorScrollPane, BorderLayout.WEST);
        add(itemScrollPane, BorderLayout.CENTER);

    }

    @Override
    public void updateComponents(Object... args) {
        doorTableModel.updateTable();
        if (doorMgr().getDoorList().size() > 0) {
            if (selectedDoor == null) {
                selectedDoor = doorMgr().getDoorList().get(0);
                doorTable.selectItem(doorMgr().getDoorList().get(0));
            }
            if (selectedDoor != null) {
                updateItemTable(selectedDoor.getItemList());
            }
        }
    }
}
