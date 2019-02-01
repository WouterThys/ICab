package com.galenus.act.classes.managers.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.galenus.act.classes.interfaces.SerialListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.galenus.act.classes.managers.serial.SerialError.ErrorType.*;

public class SerialManager {

    private static final int MAX_BUFFER = 50;

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
    private Vector<SerialListener> serialListenerList = new Vector<>();
    private SerialPort serialPort;
    private Vector<SerialMessage> txMessageList = new Vector<>(MAX_BUFFER);
    private Vector<SerialMessage> rxMessageList = new Vector<>(MAX_BUFFER);
    private Vector<SerialMessage> ackMessageList = new Vector<>(MAX_BUFFER);
    private String inputString = "";

    private volatile boolean writing = false;
    private int writeCount = 0;
    private double averageAcknowledgeTime = 0;

    private PingThread pingThread;

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void init(SerialListener serialListener) {
        serialListenerList.add(serialListener);
    }

    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    private void close() {
        if (serialPort != null) {
            // Stop pinging
            try {
                if (pingThread != null) {
                    pingThread.stop();
                }
            } catch (Exception e) {
                // Nothing we can do..
            }


                // Reset
                try {
                    SerialMessage reset = MessageFactory.createReset();
                    String data = reset.toString();
                    serialPort.writeBytes(data.getBytes(), data.length());

                    Thread.sleep(100);
                } catch (Exception e) {
                    // Nothing we can do..
                }

                // Close port
                try {
                    if (serialPort.isOpen()) {
                        SerialMessage reset = MessageFactory.createReset();
                        String data = reset.toString();
                        serialPort.writeBytes(data.getBytes(), data.length());
                    }

                    serialPort.removeDataListener();
                    serialPort.closePort();
                } catch (Exception e) {
                    // Nothing we can do..
                }

        }
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public String getInputBufferString() {
        return inputString;
    }

    public int getWriteCount() {
        return writeCount;
    }

    public double getAverageAcknowledgeTime() {
        return averageAcknowledgeTime;
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
                onError(new SerialError(SerialError.ErrorType.OpenError, "Failed to open port: " + port.getDescriptivePortName()));
            }
        }
    }

    public void reInitialize() {
            SwingUtilities.invokeLater(() -> {
                try {
                    sendReset();
                    Thread.sleep(1000);
                    sendInit(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }

    public void startPinging(int delayInMillis) {
        if (pingThread != null) {
            pingThread.cancel(true);
        }

        pingThread = new PingThread(delayInMillis);
        pingThread.execute();
    }

    public void setPingEnabled(boolean enabled) {
        if (pingThread != null && pingThread.keepRunning) {
            pingThread.setEnabled(enabled);
        }
    }

    public boolean getPingEnabled() {
        return pingThread != null && pingThread.keepRunning && pingThread.enabled;
    }

    public int getPingDelay() {
        if (pingThread != null && pingThread.keepRunning) {
            return pingThread.delay;
        } else {
            return 0;
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

    public void sendPing() {
        SerialMessage ping = MessageFactory.createPing();
        if (addToMessageList(ping)) {
            write(ping);
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

    public void write(final SerialMessage message) {
        try {
            if (serialPort != null) {
                if (serialPort.isOpen()) {
                    SwingUtilities.invokeLater(() -> {
                        doWrite(message);
                    });
                } else {
                    onError(new SerialError(OpenError,"COM port is closed.."));
                }
            } else {
                onError(new SerialError(OtherError,"No COM port available.."));
            }
        } catch (Exception e) {
            onError(new SerialError(OtherError,e, "Unexpected serial error.."));
        }
    }

    private void doWrite(SerialMessage message) {
        if (writing) {
            System.out.println("Still writing..");
        }
        int waitCount = 0;
        while (writing && waitCount < 20) {
            try {
                Thread.sleep(10);
                waitCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (writing) {
            onError(new SerialError(WriteError, null, null,"Failed to write.."));
            writing = false;
            return;
        }
        writing = true;
        try {
            String data = message.toString();
            serialPort.writeBytes(data.getBytes(), data.length());
            System.out.println("Bytes written: " + data);
            writeCount++;

            int cnt = 0;
            boolean acknowledged = false;
            while(!acknowledged && cnt < 200) {
                Thread.sleep(2);
                acknowledged = message.isAcknowledged();
                cnt++;
            }
            if (!acknowledged) {
                onError(new SerialError(WriteError, message, null,"Controller did not respond after 400ms"));
            } else {
                averageAcknowledgeTime = (((writeCount -1) * averageAcknowledgeTime) + cnt*2) / writeCount;
                onNewWrite(message);
            }
        } catch (Exception e) {
            onError(new SerialError(WriteError, message, e, "Controller did not respond after 400ms"));
        } finally {
            writing = false;
        }
    }

    private boolean addToMessageList(SerialMessage message) {
        for (SerialMessage m : txMessageList) {
            if (m.getId() == message.getId()) {
                return false;
            }
        }
        txMessageList.add(message);
        if (txMessageList.size() >= MAX_BUFFER) {
            txMessageList.remove(0);
        }
        return true;
    }

    private void onError(SerialError error) {
        for (SerialListener listener : serialListenerList) {
            listener.onSerialError(error);
        }
    }

    private void onInitSuccess(SerialPort serialPort) {
        for (SerialListener listener : serialListenerList) {
            listener.onInitSuccess(serialPort);
        }
    }

    private void onNewWrite(SerialMessage message) {
        for (SerialListener listener : serialListenerList) {
            listener.onNewWrite(message);
        }
    }

    private void onNewRead(SerialMessage message) {
        for (SerialListener listener : serialListenerList) {
            listener.onNewRead(message);
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
                        if (ackMessageList.size() >= MAX_BUFFER) {
                            ackMessageList.remove(0);
                        }
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
                    onNewRead(message);
                } else {
                    rxMessageList.add(message);
                    if (rxMessageList.size() >= MAX_BUFFER) {
                        rxMessageList.remove(0);
                    }
                    onNewRead(message);
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
                    onError(new SerialError(ReadError, "Invalid input.."));
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
                    serialListener.onSerialError(new SerialError(OpenError, "Failed finding COM port.."));
                }
            } catch (Exception e) {
                serialListener.onSerialError(new SerialError(OpenError, e,"Failed finding COM port.."));
            }
        }
    }

    private static class PingThread extends SwingWorker<Void, Void> {

        private boolean keepRunning = true;
        private boolean enabled = true;

        private int delay;

        public PingThread(int delay) {
            this.delay = delay;
            this.keepRunning = true;
            this.enabled = true;
        }

        void stop() {
            keepRunning = false;
        }

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        protected Void doInBackground() throws Exception {

            while (keepRunning) {
                try {
                    if (enabled) {
                        serMgr().sendPing();
                    }
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
