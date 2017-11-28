package com.galenus.act.gui.dialogs.logsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.Main;
import com.galenus.act.classes.interfaces.GuiInterface;
import com.galenus.act.gui.Application;
import com.galenus.act.gui.components.ILabel;
import com.galenus.act.gui.components.ITable;
import com.galenus.act.gui.components.ITextField;
import com.galenus.act.gui.models.SerialLogTableModel;
import com.galenus.act.serial.SerialMessage;
import com.galenus.act.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.galenus.act.gui.Application.imageResource;
import static com.galenus.act.serial.SerialManager.serMgr;

class SerialLogsPanel extends JPanel implements GuiInterface {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private ILabel serialStateLbl;
    private ILabel serialNameLbl;
    private ILabel serialBufferStringLbl;
    private ITextField serialBaudTf;
    private ITextField serialDataBitsTf;
    private ITextField serialStopBitsTf;
    private ITextField serialParityTf;
    private ITextField serialReadTimeoutTf;
    private ITextField serialWriteTimeoutTf;

    private AbstractAction initAa;
    private AbstractAction resetAa;
    private AbstractAction lockAa;
    private AbstractAction unlockAa;
    private AbstractAction errorAa;

    private AbstractAction deleteRxAa;
    private AbstractAction deleteTxAa;
    private AbstractAction retryAa;

    private SerialLogTableModel logTxModel;
    private ITable<SerialMessage> logTxTable;

    private SerialLogTableModel logRxModel;
    private ITable<SerialMessage> logRxTable;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    SerialLogsPanel() {
        initializeComponents();
        initializeLayouts();
    }


    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private void onRetry() {
        //application.initializeSerial(LogsDialog.this, serMgr().getMainSerialListener());
    }

    private void onDeleteRx() {
        int res = JOptionPane.showConfirmDialog(
                SerialLogsPanel.this,
                "Delete all received messages?",
                "Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (res == JOptionPane.YES_OPTION) {
            serMgr().clearRxMessages();
            updateTableData();
        }
    }

    private void onDeleteTx() {
        int res = JOptionPane.showConfirmDialog(
                SerialLogsPanel.this,
                "Delete all send messages?",
                "Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (res == JOptionPane.YES_OPTION) {
            serMgr().clearTxMessages();
            updateTableData();
        }
    }
    private void onPicInit() {
        serMgr().sendInit(Main.DOOR_COUNT);
    }
    private void onPicReset() {
        serMgr().sendReset();
    }
    private void onPicLock() {
        serMgr().sendLockAll();
    }
    private void onPicUnlock() {
        serMgr().sendUnlockAll();
    }
    private void onPicError() {

    }

    private void setSerialData(SerialPort port) {
        if (port != null) {
            serialNameLbl.setText(port.getDescriptivePortName());
            serialBaudTf.setText(String.valueOf(port.getBaudRate()));
            serialDataBitsTf.setText(String.valueOf(port.getNumDataBits()));
            serialStopBitsTf.setText(String.valueOf(port.getNumStopBits()));
            serialParityTf.setText(String.valueOf(port.getParity()));
            serialReadTimeoutTf.setText(String.valueOf(port.getReadTimeout()));
            serialWriteTimeoutTf.setText(String.valueOf(port.getWriteTimeout()));
            serialBufferStringLbl.setText(serMgr().getInputBufferString());
            if (port.isOpen()) {
                serialStateLbl.setIcon(imageResource.readImage("Serial.Port.Ok"));
            } else {
                serialStateLbl.setIcon(imageResource.readImage("Serial.Port.Nok"));
            }
            initAa.setEnabled(true);
            resetAa.setEnabled(true);
            lockAa.setEnabled(true);
            unlockAa.setEnabled(true);
            errorAa.setEnabled(true);
        } else {
            serialNameLbl.setText("");
            serialBaudTf.clearText();
            serialDataBitsTf.clearText();
            serialStopBitsTf.clearText();
            serialReadTimeoutTf.clearText();
            serialWriteTimeoutTf.clearText();
            serialStateLbl.setIcon(imageResource.readImage("Serial.Port.Nok"));
            serialBufferStringLbl.setText("");
            initAa.setEnabled(false);
            resetAa.setEnabled(false);
            lockAa.setEnabled(false);
            unlockAa.setEnabled(false);
            errorAa.setEnabled(false);
        }
    }

    void updateTableData() {
        java.util.List<SerialMessage> txList = new ArrayList<>();
        List<SerialMessage> rxList = new ArrayList<>();

        txList.addAll(serMgr().getTxMessageList());
        txList.addAll(serMgr().getAckMessageList());
        rxList.addAll(serMgr().getRxMessageList());

        txList.sort(new SerialLogsPanel.LogMessageSorter());
        rxList.sort(new SerialLogsPanel.LogMessageSorter());

        logRxModel.setItemList(rxList);
        logTxModel.setItemList(txList);
    }

    private JPanel createTxPanel() {
        JPanel txPanel = new JPanel(new BorderLayout());

        // Table
        JScrollPane scrollTxPane = new JScrollPane(logTxTable);
        scrollTxPane.setPreferredSize(new Dimension(600,200));

        // Toolbar
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        toolBar.add(deleteTxAa);

        // Extra
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new ILabel("Send messages: "), BorderLayout.CENTER);
        topPanel.add(toolBar, BorderLayout.EAST);

        // Add
        txPanel.add(topPanel, BorderLayout.PAGE_START);
        txPanel.add(scrollTxPane, BorderLayout.CENTER);
        txPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        return txPanel;
    }

    private JPanel createRxPanel() {
        JPanel rxPanel = new JPanel(new BorderLayout());

        // Table
        JScrollPane scrollRxPane = new JScrollPane(logRxTable);
        scrollRxPane.setPreferredSize(new Dimension(600,200));

        // Toolbar
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        toolBar.add(deleteRxAa);

        // Extra
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new ILabel("Received messages: "), BorderLayout.CENTER);
        topPanel.add(toolBar, BorderLayout.EAST);

        // Add
        rxPanel.add(topPanel, BorderLayout.PAGE_START);
        rxPanel.add(scrollRxPane, BorderLayout.CENTER);
        rxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        return rxPanel;
    }

    private JPanel createSerialPanel() {
        JPanel serialPanel = new JPanel(new BorderLayout());

        // Extra
        JToolBar retryTb = new JToolBar();
        retryTb.setFloatable(false);
        retryTb.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        retryTb.add(retryAa);

        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.add(retryTb, BorderLayout.WEST);
        iconPanel.add(serialNameLbl, BorderLayout.CENTER);
        iconPanel.add(serialStateLbl, BorderLayout.EAST);
        GuiUtils.GridBagHelper gbc;

        JPanel serialDataPanel1 = new JPanel(new GridBagLayout());
        gbc = new GuiUtils.GridBagHelper(serialDataPanel1);
        gbc.addLine("Baud: ", serialBaudTf);
        gbc.addLine("Data bits: ", serialDataBitsTf);
        gbc.addLine("Stop bits: ", serialStopBitsTf);

        JPanel serialDataPanel2 = new JPanel(new GridBagLayout());
        gbc = new GuiUtils.GridBagHelper(serialDataPanel2);
        gbc.addLine("Parity: ", serialParityTf);
        gbc.addLine("Read timeout (ms): ", serialReadTimeoutTf);
        gbc.addLine("Write timeout (ms): ", serialWriteTimeoutTf);

        JToolBar testToolbar = new JToolBar(JToolBar.VERTICAL);
        testToolbar.setFloatable(false);
        testToolbar.add(initAa);
        testToolbar.add(resetAa);
        testToolbar.add(errorAa);
        testToolbar.addSeparator();
        testToolbar.add(lockAa);
        testToolbar.add(unlockAa);

        JPanel serialDataPanel = new JPanel(new BorderLayout());
        JPanel dataPanels = new JPanel();
        dataPanels.add(serialDataPanel1);
        dataPanels.add(serialDataPanel2);
        serialDataPanel.add(iconPanel, BorderLayout.NORTH);
        serialDataPanel.add(dataPanels, BorderLayout.CENTER);
        serialDataPanel.add(serialBufferStringLbl, BorderLayout.SOUTH);

        serialPanel.add(serialDataPanel, BorderLayout.CENTER);
        serialPanel.add(testToolbar, BorderLayout.EAST);
        serialPanel.setBorder(GuiUtils.createTitleBorder("Serial"));

        return serialPanel;
    }

    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        // Serial
        serialStateLbl = new ILabel();
        serialNameLbl = new ILabel("", ILabel.CENTER);
        serialNameLbl.setFont(20, Font.BOLD);
        serialBaudTf = new ITextField(false);
        serialDataBitsTf = new ITextField(false);
        serialStopBitsTf = new ITextField(false);
        serialParityTf = new ITextField(false);
        serialReadTimeoutTf = new ITextField(false);
        serialWriteTimeoutTf = new ITextField(false);
        serialBufferStringLbl = new ILabel();
        serialBufferStringLbl.setForeground(Color.gray);

        // Tables
        logTxModel = new SerialLogTableModel();
        logTxTable = new ITable<>(logTxModel);
        logTxTable.setExactColumnWidth(0, 36);
        logTxTable.setExactColumnWidth(5, 40);

        logRxModel = new SerialLogTableModel();
        logRxTable = new ITable<>(logRxModel);
        logRxTable.setExactColumnWidth(0, 36);
        logRxTable.hide("Ack");

        // Test
        initAa = new AbstractAction("Init", imageResource.readImage("Serial.Test.Init")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPicInit();
            }
        };
        initAa.putValue(AbstractAction.SHORT_DESCRIPTION, "Init");
        resetAa = new AbstractAction("Reset", imageResource.readImage("Serial.Test.Replace")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPicReset();
            }
        };
        resetAa.putValue(AbstractAction.SHORT_DESCRIPTION, "Reset");
        lockAa = new AbstractAction("Lock", imageResource.readImage("Serial.Test.Lock")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPicLock();
            }
        };
        lockAa.putValue(AbstractAction.SHORT_DESCRIPTION, "Lock");
        unlockAa = new AbstractAction("Unlock", imageResource.readImage("Serial.Test.Unlock")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPicUnlock();
            }
        };
        unlockAa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unlock");
        errorAa = new AbstractAction("Error", imageResource.readImage("Serial.Test.Error")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPicError();
            }
        };
        errorAa.putValue(AbstractAction.SHORT_DESCRIPTION, "Error");

        deleteRxAa = new AbstractAction("Delete logs") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDeleteRx();
            }
        };
        deleteTxAa = new AbstractAction("Delete logs") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDeleteTx();
            }
        };
        retryAa = new AbstractAction("Retry", imageResource.readImage("Serial.Retry")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRetry();
            }
        };
    }

    @Override
    public void initializeLayouts() {
        JPanel serialPanel = createSerialPanel();
        JPanel txPanel = createTxPanel();
        JPanel rxPanel = createRxPanel();

        // Add
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(txPanel);
        tablePanel.add(rxPanel);
        tablePanel.setBorder(GuiUtils.createTitleBorder("Logs"));

        setLayout(new BorderLayout());
        add(serialPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    @Override
    public void updateComponents(Object... object) {
        // Serial port
        if (object.length > 0 && object[0] != null) {
            setSerialData((SerialPort) object[0]);
        } else {
            setSerialData(null);
        }

        // Tables
        updateTableData();
    }

    private static class LogMessageSorter implements Comparator<SerialMessage> {
        @Override
        public int compare(SerialMessage o1, SerialMessage o2) {
            if (o1 == null && o2 != null) return -1;
            if (o1 != null && o2 == null) return 1;
            if (o1 == null) return 0;

            if (o1.isAcknowledged() && !o2.isAcknowledged()) return 1;
            if (!o1.isAcknowledged() && o2.isAcknowledged()) return -1;

            return -(o1.getDate().compareTo(o2.getDate()));
        }
    }
}
