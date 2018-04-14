package com.galenus.act.gui.dialogs.logsdialog;

import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.gui.components.ILabel;
import com.galenus.act.gui.components.ITable;
import com.galenus.act.gui.models.WebLogTableModel;
import com.galenus.act.utils.GuiUtils;
import com.galenus.act.classes.managers.web.AsyncWebCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.galenus.act.Application.imageResource;
import static com.galenus.act.classes.managers.web.WebManager.webMgr;

class WebLogsPanel extends JPanel implements GuiInterface {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private ILabel webStateLbl;
    private ILabel webUrlLbl;
    private ILabel webDeviceNameLbl;

    private AbstractAction deleteLogsAa;

    private WebLogTableModel webLogModel;
    private ITable<AsyncWebCall> webLogTable;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    WebLogsPanel() {
        initializeComponents();
        initializeLayouts();
    }


    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void onDeleteLogs() {
        int res = JOptionPane.showConfirmDialog(
                WebLogsPanel.this,
                "Delete all logs?",
                "Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (res == JOptionPane.YES_OPTION) {
            webMgr().clearWebCallList();
            updateTableData();
        }
    }

    void updateTableData() {
        List<AsyncWebCall> webCalls = new ArrayList<>();
        webCalls.addAll(webMgr().getWebCallList());

        webCalls.sort(new WebLogsPanel.LogMessageSorter());

        webLogModel.setItemList(webCalls);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        JScrollPane scrollRxPane = new JScrollPane(webLogTable);
        scrollRxPane.setPreferredSize(new Dimension(600,200));

        // Toolbar
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        toolBar.add(deleteLogsAa);

        // Extra
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new ILabel("Logs: "), BorderLayout.CENTER);
        topPanel.add(toolBar, BorderLayout.EAST);

        // Add
        panel.add(topPanel, BorderLayout.PAGE_START);
        panel.add(scrollRxPane, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        return panel;
    }

    private JPanel createWebInfoPanel() {
        JPanel webPanel = new JPanel(new BorderLayout());

        // Extra

        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.add(webDeviceNameLbl, BorderLayout.CENTER);
        iconPanel.add(webStateLbl, BorderLayout.EAST);
        GuiUtils.GridBagHelper gbc;

        JPanel webDataPanel = new JPanel(new GridBagLayout());
        gbc = new GuiUtils.GridBagHelper(webDataPanel);
        gbc.addLine("Url: ", webUrlLbl);

        webPanel.add(iconPanel, BorderLayout.NORTH);
        webPanel.add(webDataPanel, BorderLayout.CENTER);
        webPanel.setBorder(GuiUtils.createTitleBorder("Web info"));

        return webPanel;
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        // Web
        webStateLbl = new ILabel();
        webUrlLbl = new ILabel();
        webDeviceNameLbl = new ILabel();
        webDeviceNameLbl = new ILabel("", ILabel.CENTER);
        webDeviceNameLbl.setFont(20, Font.BOLD);

        // Tables
        webLogModel = new WebLogTableModel();
        webLogTable = new ITable<>(webLogModel);
        webLogTable.setExactColumnWidth(2, 36);

        // Actions
        deleteLogsAa = new AbstractAction("Delete logs") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDeleteLogs();
            }
        };
    }

    @Override
    public void initializeLayouts() {
        JPanel infoPanel = createWebInfoPanel();
        JPanel tablePanel = createTablePanel();

        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... object) {
        if (webMgr().isWebSuccess()) {
            webStateLbl.setIcon(imageResource.readImage("Web.Init.Ok"));
        } else {
            webStateLbl.setIcon(imageResource.readImage("Web.Init.Nok"));
        }
        webUrlLbl.setText(webMgr().getWebUrl());
        webDeviceNameLbl.setText(webMgr().getDeviceName());

        // Tables
        updateTableData();
    }

    private static class LogMessageSorter implements Comparator<AsyncWebCall> {
        @Override
        public int compare(AsyncWebCall o1, AsyncWebCall o2) {
            return -(o1.getDate().compareTo(o2.getDate()));
        }
    }
}
