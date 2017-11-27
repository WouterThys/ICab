package com.galenus.act.gui.models;

import com.galenus.act.classes.Item;

import javax.swing.*;

import java.util.List;

import static com.galenus.act.gui.Application.imageResource;

public class ItemTableModel extends IAbstractTableModel<Item> {

    private static final String[] COLUMN_NAMES = {"", "Code", "Description", "Location"};
    private static final Class[] COLUMN_CLASSES = {ImageIcon.class, String.class, String.class, String.class};

    private static final ImageIcon itemOkIcon = imageResource.readImage("Item.Table.AmountOk");
    private static final ImageIcon itemNokIcon = imageResource.readImage("Item.Table.AmountNok");


    public ItemTableModel(List<Item> itemList) {
        super(COLUMN_NAMES, COLUMN_CLASSES);
        setItemList(itemList);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item item = getItemAt(rowIndex);

        if (item != null) {
            switch (columnIndex) {
                case -1:
                    return item;
                case 0: // Amount
                    if (item.getAmount() > 0) {
                        return itemOkIcon;
                    } else {
                        return itemNokIcon;
                    }
                case 1: // Code
                    return item.getCode();
                case 2: // Description
                    return item.getDescription();
                case 3: // Location
                    return item.getLocation();
            }
        }
        return null;
    }
}
