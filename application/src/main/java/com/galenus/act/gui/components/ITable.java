package com.galenus.act.gui.components;

import com.galenus.act.gui.models.IAbstractTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ITable<T> extends JTable {

    private IAbstractTableModel<T> model;
    private boolean autoAdaptHeight;
    private Map hiddenColumns;

    public ITable(IAbstractTableModel<T> model) {
        super(model);

        this.model = model;
        this.autoAdaptHeight = false;
        this.hiddenColumns = new HashMap();

        setModel(model);
        setRowHeight(25);

        setPreferredScrollableViewportSize(getPreferredSize());
        setAutoCreateRowSorter(true);

        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public ITable(IAbstractTableModel<T> model, boolean autoSetHeight) {
        super(model);

        this.model = model;
        this.autoAdaptHeight = autoSetHeight;
        this.hiddenColumns = new HashMap();

        setModel(model);
        setRowHeight(25);

        setPreferredScrollableViewportSize(getPreferredSize());
        setAutoCreateRowSorter(true);

        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        if (autoAdaptHeight) {
            return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
        } else {
            return super.getPreferredScrollableViewportSize();
        }
    }

//    @Override
//    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//        // Width
//        Component component = super.prepareRenderer(renderer, row, column);
//        int rendererWidth = component.getPreferredSize().width;
//        TableColumn tableColumn = getColumnModel().getColumn(column);
//        tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
//
//        return component;
//    }


    public void hide(String columnName) {
        int index = getColumnModel().getColumnIndex(columnName);
        TableColumn column = getColumnModel().getColumn(index);
        hiddenColumns.put(columnName, column);
        hiddenColumns.put(":" + columnName, new Integer(index));
        getColumnModel().removeColumn(column);
    }

    public void show(String columnName) {
        Object o = hiddenColumns.remove(columnName);
        if (o == null) {
            return;
        }
        getColumnModel().addColumn((TableColumn) o);
        o = hiddenColumns.remove(":" + columnName);
        if (o == null) {
            return;
        }
        int column = ((Integer) o).intValue();
        int lastColumn = getColumnModel().getColumnCount() - 1;
        if (column < lastColumn) {
            getColumnModel().moveColumn(lastColumn, column);
        }
    }

    @SuppressWarnings("unchecked")
    public T getValueAtRow(int row) {
        if (row >= 0) {
            return (T) model.getValueAt(convertRowIndexToModel(row), -1);
        }
        return null;
    }

    public void selectItem(T item) {
        if (item != null) {
            int row = model.getModelIndex(item);
            if (row >= 0) {
                int real = convertRowIndexToView(row);
                setRowSelectionInterval(real, real);
            }
        } else {
            clearSelection();
        }
    }

    public T getSelectedItem() {
        int row = getSelectedRow();
        return getValueAtRow(row);
    }

    public List<T> getSelectedItems() {
        List<T> list = new ArrayList<>();
        int[] selectedRows = getSelectedRows();
        if (selectedRows.length > 0) {
            for (int row : selectedRows) {
                T t = getValueAtRow(row);
                if (t != null) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    @Override
    protected JTableHeader createDefaultTableHeader() {
        IAbstractTableModel model = (IAbstractTableModel) getModel();
        if ((model != null) &&
                (model.getColumnHeaderToolTips() != null) &&
                (model.getColumnHeaderToolTips().length == model.getColumnCount())) {
            return new JTableHeader(columnModel) {
                @Override
                public String getToolTipText(MouseEvent event) {
                    Point p = event.getPoint();
                    int ndx = columnModel.getColumnIndexAtX(p.x);
                    int realNdx = columnModel.getColumn(ndx).getModelIndex();
                    return model.getColumnHeaderToolTips()[realNdx];
                }
            };
        }
        return super.createDefaultTableHeader();
    }

    public void setExactColumnWidth(int column, int width) {
        TableColumn tableColumn = getColumnModel().getColumn(column);
        if (tableColumn != null) {
            tableColumn.setMaxWidth(width);
            tableColumn.setMinWidth(width);
        }
    }
}