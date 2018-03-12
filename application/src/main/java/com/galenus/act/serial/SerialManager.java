package com.galenus.act.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.galenus.act.classes.interfaces.SerialListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SerialManager {

    /*
     *                  SINGLETON
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static final SerialManager Instance = new SerialManager();
    public static SerialManager serMgr() {
        return Instance;
    }
    private SerialManager() {
    }

    /*
     *                  STATICS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /*
     *                  VARIABLES
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<SerialListener> serialListenerList = new ArrayList<>();
    private SerialPort serialPort;
    private List<SerialMessage> txMessageList = new ArrayList<>();
    private List<SerialMessage> rxMessageList = new ArrayList<>();
    private List<SerialMessage> ackMessageList = new ArrayList<>();
    private String inputString = "";

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void init(SerialListener serialListener) {
        serialListenerList.add(serialListener);
    }

    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public void close() {
        if (serialPort != null) {
            try {
                if (serialPort.isOpen()) {
                    SerialMessage reset = MessageFactory.createReset();
                    String data = reset.toString();
                    serialPort.writeBytes(data.getBytes(), data.length());
                }

                serialPort.removeDataListener();
                serialPort.closePort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public String getInputBufferString() {
        return inputString;
    }

    public void clearRxMessages() {
        rxMessageList.clear();
    }

    public void clearTxMessages() {
        txMessageList.clear();
        ackMessageList.clear();
    }

    public void addSerialListener(SerialListener serialListener) {
        if (!serialListenerList.contains(serialListener)) {
            serialListenerList.add(serialListener);
        }
    }

    public void removeSerialListener(SerialListener serialListener) {
        if (serialListenerList.contains(serialListener)) {
            serialListenerList.remove(serialListener);
        }
    }

    public SerialListener getMainSerialListener() {
        if (serialListenerList.size() > 0) {
            return serialListenerList.get(0);
        }
        return null;
    }

    public void initComPort(SerialPort port) {
        if (port != null) {
            this.serialPort = port;
            this.serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            addDataAvailableEvent(this.serialPort);
            if (!this.serialPort.openPort()) {
                onError("Failed to open port: " + port.getDescriptivePortName());
            }
        }
    }

    public void sendReset() {
        SerialMessage reset = MessageFactory.createReset();
        if (addToMessageList(reset)) {
            write(reset);
        }
    }

    public void sendInit(int doorCount) {
        SerialMessage init = MessageFactory.createInit(doorCount);
        if (addToMessageList(init)) {
            write(init);
        }
    }

    public void sendAlarm(int strength) {
        SerialMessage alarm = MessageFactory.createAlarm(strength);
        if (addToMessageList(alarm)) {
            write(alarm);
        }
    }

    public void sendLockAll() {
        SerialMessage lock = MessageFactory.createLockAll();
        if (addToMessageList(lock)) {
            write(lock);
        }
    }

    public void sendUnlockAll() {
        SerialMessage unlock = MessageFactory.createUnlockAll();
        if (addToMessageList(unlock)) {
            write(unlock);
        }
    }

    public List<SerialMessage> getTxMessageList() {
        return new ArrayList<>(txMessageList);
    }

    public List<SerialMessage> getAckMessageList() {
        return new ArrayList<>(ackMessageList);
    }

    public List<SerialMessage> getRxMessageList() {
        return new ArrayList<>(rxMessageList);
    }

    private void write(final SerialMessage message) {
        try {
            if (serialPort != null) {
                if (serialPort.isOpen()) {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            String data = message.toString();
                            serialPort.writeBytes(data.getBytes(), data.length());
                            System.out.println("Bytes written: " + data);

                            Thread.sleep(80);
                            if (!message.isAcknowledged()) {
                                onError("Controller did not respond");
                            }
                        } catch (Exception e) {
                            onError(e);
                        }
                    });
                } else {
                    onError("COM port is closed..");
                }
            } else {
                onError("No COM port available..");
            }
        } catch (Exception e) {
            onError(e);
        }
    }

    private boolean addToMessageList(SerialMessage message) {
        for (SerialMessage m : txMessageList) {
            if (m.getId() == message.getId()) {
                onError("Buffer overflow: message with same id found..");
                return false;
            }
        }
        txMessageList.add(message);
        return true;
    }

    private void onError(Throwable throwable) {
        onError(throwable.getMessage());
    }

    private void onError(String error) {
        for (SerialListener listener : serialListenerList) {
            listener.onSerialError(error);
        }
    }

    private void onInitSuccess(SerialPort serialPort) {
        for (SerialListener listener : serialListenerList) {
            listener.onInitSuccess(serialPort);
        }
    }

    private void onNewMessage(SerialMessage message) {
        for (SerialListener listener : serialListenerList) {
            listener.onNewMessage(message);
        }
    }

    private void addDataAvailableEvent(SerialPort serialPort) {
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    SerialPort comPort = event.getSerialPort();
                    byte[] bytes = new byte[comPort.bytesAvailable()];
                    comPort.readBytes(bytes, bytes.length);
                    newDataAvailable(bytes);
                }
            }
        });
    }

    private void tryAcknowledge(SerialMessage ack) {
        if (ack != null) {
            if (ack.getType().equals(SerialMessage.Acknowledge)) {
                List<SerialMessage> copy = new ArrayList<>(txMessageList);
                for (SerialMessage message : copy) {
                    if (message.getId() == ack.getId()) {
                        message.setAcknowledged(ack);

                        System.out.println("Acknowledge message: " + message);

                        ackMessageList.add(message);
                        txMessageList.remove(message);
                        return;
                    }
                }
            }
        }
    }

    private int retry = 0;
    private void newDataAvailable(byte[] newData) {
        //System.out.println("New data received: " + new String(newData));
        inputString += new String(newData);

        // Do magic
        SerialMessage message;
        do {
            message = MessageFactory.deserialize(inputString);
            if (message != null) {
                if (message.getType().equals(SerialMessage.Acknowledge)) {
                    tryAcknowledge(message);
                    onNewMessage(message);
                } else {
                    rxMessageList.add(message);
                    onNewMessage(message);
                }
                if (inputString.contains(message.toString())) {
                    retry = 0;
                    inputString = inputString.replace(message.toString(), "");
                } else {
                    retry ++;
                }
                if (retry > 5) {
                    retry = 0;
                    inputString = "";
                    onError("Invalid buffer exceeded retry count..");
                }
            }
        } while (message != null);
    }



    public static class FindComPortThread extends SwingWorker<Boolean, Integer> {

        private final SerialListener serialListener;
        private SerialPort serialPort;
        private SerialPort[] serialPorts;

        public FindComPortThread(SerialListener serialListener) {
            this.serialListener = serialListener;
            this.serialPort = null;

            this.serialPorts = SerialPort.getCommPorts();
        }

        int getNumSerialPorts() {
            if (serialPorts != null) {
                return serialPorts.length;
            }
            return 0;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            serialPort = null;

            int cnt = 0;
            if (getNumSerialPorts() > 0) {
                for (SerialPort port : serialPorts) {
                    if (!isCancelled()) {
                        cnt++;
                        port.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

                        double progress = (double) cnt / (double) getNumSerialPorts();
                        setProgress((int) (progress * 100));

                        port.openPort();
                        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 200, 100);
                        try {
                            byte[] readBuffer = new byte[1024];

                            // Write reset
                            String ping = MessageFactory.createPing().toString();
                            port.writeBytes(ping.getBytes(), ping.length());

                            // Wait for read
                            int numRead = port.readBytes(readBuffer, readBuffer.length);
                            if (numRead > 0) {
                                String answer = new String(readBuffer);
                                if (answer.contains(SerialMessage.Acknowledge)) { // Success!!
                                    serialPort = port;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            port.closePort();
                        }
                    }
                }
            }
            return (serialPort != null);
        }

        @Override
        protected void done() {
            try {
                boolean success = get();
                if (success) {
                    serialListener.onInitSuccess(serialPort);
                } else {
                    serialListener.onSerialError("OpenWhileLocked finding COM port..");
                }
            } catch (Exception e) {
                e.printStackTrace();
                serialListener.onSerialError("OpenWhileLocked initializing: " + e.getMessage());
            }
        }
    }
}
