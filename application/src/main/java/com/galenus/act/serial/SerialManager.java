package com.galenus.act.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

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
    private SerialListener serialListener;
    private SerialPort serialPort;
    private List<SerialMessage> messageList = new ArrayList<>();
    private List<SerialMessage> acknowledgedList = new ArrayList<>();
    private String inputString = "";

    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void init(SerialListener serialListener) {
        this.serialListener = serialListener;
    }

    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public void close() {
        if (serialPort != null) {
            try {
                serialPort.removeDataListener();
                serialPort.closePort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initComPort(SerialPort port) {
        if (port != null) {
            this.serialPort = port;
            this.serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            addDataAvailableEvent(this.serialPort);
            if (!this.serialPort.openPort()) {
                setError("Failed to open port: " + port.getDescriptivePortName());
            }
        }
    }

    public void sendReset() {
        SerialMessage reset = MessageFactory.createReset();
        addToMessageList(reset);
        write(reset.toString());
    }

    public void sendInit() {
        SerialMessage init = MessageFactory.createInit();
        addToMessageList(init);
        write(init.toString());
    }

    public void sendLockAll() {
        SerialMessage lock = MessageFactory.createLockAll();
        addToMessageList(lock);
        write(lock.toString());
    }

    public void sendUnlockAll() {
        SerialMessage unlock = MessageFactory.createUnlockAll();
        addToMessageList(unlock);
        write(unlock.toString());
    }

    private void write(String data) {
        try {
            if (serialPort != null) {
                if (serialPort.isOpen()) {
                    serialPort.writeBytes(data.getBytes(), data.length());
                    System.out.println("Bytes written: " + data);
                } else {
                    setError("COM port is closed..");
                }
            } else {
                setError("No COM port available..");
            }
        } catch (Exception e) {
            setError(e);
        }
    }

    private void addToMessageList(SerialMessage message) {
        for (SerialMessage m : messageList) {
            if (m.getId() == message.getId()) {
                serialListener.onSerialError("Buffer overflow: message with same id found..");
                return;
            }
        }
        messageList.add(message);
    }

    private void setError(Throwable throwable) {
        setError(throwable.getMessage());
    }

    private void setError(String error) {
        if (serialListener != null) {
            serialListener.onSerialError(error);
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
                List<SerialMessage> copy = new ArrayList<>(messageList);
                for (SerialMessage message : copy) {
                    if (message.getId() == ack.getId()) {
                        message.setAcknowledged();

                        acknowledgedList.add(message);
                        messageList.remove(message);
                        return;
                    }
                }
            }
        }
    }

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
                } else {
                    if (serialListener != null) {
                        serialListener.onNewMessage(message);
                    }
                }
                inputString = inputString.replace(message.toString(), "");
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
                        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 500, 100);
                        try {
                            byte[] readBuffer = new byte[1024];

                            // Write reset
                            String reset = MessageFactory.createInit().toString();
                            port.writeBytes(reset.getBytes(), reset.length());

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
                    serialListener.onSerialError("Error finding COM port..");
                }
            } catch (Exception e) {
                e.printStackTrace();
                serialListener.onSerialError("Error initializing: " + e.getMessage());
            }
        }
    }
}
