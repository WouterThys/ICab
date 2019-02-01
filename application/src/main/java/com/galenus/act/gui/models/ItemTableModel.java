package com.galenus.act.gui.models;

import com.galenus.act.classes.Item;
import com.galenus.act.gui.components.ILabel;
import com.galenus.act.gui.components.ITableLabel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

import static com.galenus.act.Application.imageResource;

public class ItemTableModel extends IAbstractTableModel<Item> {

    private static final String[] COLUMN_NAMES = {"", "Code", "Description", "Location"};
    private static final Class[] COLUMN_CLASSES = {ILabel.class, String.class, String.class, String.class};

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
                    return item;
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

    public static class ItemTableRenderer extends DefaultTableCellRenderer {

        private static final ImageIcon itemOkIcon = imageResource.readImage("Item.Table.AmountOk");
        private static final ImageIcon itemNokIcon = imageResource.readImage("Item.Table.AmountNok");
        private static final ITableLabel stateLabel = new ITableLabel(Color.gray, 0, false, itemOkIcon);

        private boolean first = true;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Item) {

                if (first && row == 0) {
                    TableColumn tableColumn = table.getColumnModel().getColumn(column);
                    tableColumn.setMaxWidth(32);
                    tableColumn.setMinWidth(32);
                    first = false;
                }

                Item item = (Item) value;

                String amount = String.valueOf(item.getAmount());

                stateLabel.updateWithTableComponent(component, row, isSelected);
                stateLabel.setText(amount);

                if (item.getAmount() > 0) {
                    stateLabel.setIcon(itemOkIcon);
                } else {
                    stateLabel.setIcon(itemNokIcon);
                }

                return stateLabel;
            }

            return component;
        }
    }
}
