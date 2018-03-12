package com.galenus.act.gui.panels.inventory;

import com.galenus.act.classes.Door;
import com.galenus.act.classes.Item;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.classes.managers.DoorManager;
import com.galenus.act.gui.components.ITable;
import com.galenus.act.gui.models.DoorTableModel;
import com.galenus.act.gui.models.ItemTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

abstract class InventoryPanelLayout extends JPanel implements GuiInterface {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private DoorTableModel doorTableModel;
    private ITable<Door> doorTable;

    private ItemTableModel itemTableModel;
    private ITable<Item> itemTable;

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    Door selectedDoor;

    /*
    *                  CONSTRUCTOR
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    InventoryPanelLayout() {

    }

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */



    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        // Doors
        doorTableModel = new DoorTableModel(DoorManager.doorMgr().getDoorList());
        doorTable = new ITable<>(doorTableModel);
        doorTable.setRowHeight(36);
        doorTable.setExactColumnWidth(0, 36);

        // Items
        itemTableModel = new ItemTableModel(new ArrayList<>());
        itemTable = new ITable<>(itemTableModel);
        itemTable.setExactColumnWidth(0, 36);
    }

    @Override
    public void initializeLayouts() {
        setLayout(new BorderLayout());

        JScrollPane doorScrollPane = new JScrollPane(doorTable);
        doorScrollPane.setPreferredSize(new Dimension(200, 400));
        JScrollPane itemScrollPane = new JScrollPane(itemTable);

        add(doorScrollPane, BorderLayout.WEST);
        add(itemScrollPane, BorderLayout.CENTER);

    }

    @Override
    public void updateComponents(Object... args) {
        doorTableModel.updateTable();
    }
}
