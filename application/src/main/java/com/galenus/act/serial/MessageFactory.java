package com.galenus.act.serial;

public class MessageFactory {

    private MessageFactory() {
    }

    private static final String PIC_INIT = "I";
    private static final String PIC_RESET = "R";
    private static final String PIC_PING = "P";
    private static final String PIC_LOCK = "L";
    private static final String PIC_UNLOCK = "U";
    private static final String PIC_ERROR = "E";

    public static SerialMessage createInit(int doorCount) {
        return new SerialMessage(SerialMessage.Message, PIC_INIT, String.valueOf(doorCount));
    }

    public static SerialMessage createReset() {
        return new SerialMessage(SerialMessage.Message, PIC_RESET);
    }

    public static SerialMessage createPing() {
        return new SerialMessage(SerialMessage.Message, PIC_PING);
    }

    public static SerialMessage createLockAll() {
        return new SerialMessage(SerialMessage.Message, PIC_LOCK);
    }

    public static SerialMessage createUnlockAll() {
        return new SerialMessage(SerialMessage.Message, PIC_UNLOCK);
    }

    public static SerialMessage createError() {
        return new SerialMessage(SerialMessage.Message, PIC_ERROR);
    }

    public static SerialMessage createAcknowledge(int id) {
        return new SerialMessage(id, SerialMessage.Acknowledge, "", "");
    }

    public static SerialMessage createReceived(String type, String sender, String command, String message) {
        return new SerialMessage(-1, sender, type, command, message);
    }

    public static SerialMessage deserialize(String inputString) {
        SerialMessage message = null;
        if (inputString != null && !inputString.isEmpty()) {
            try {
                int start = inputString.indexOf(SerialMessage.StartChar);
                int stop = inputString.indexOf(SerialMessage.StopChar);
                if (start >= 0 && stop > start) {
                    // Cut off valid part
                    String valid = inputString.substring(start + 1, stop);

                    // Acknowledge (ex: &[A]2$)
                    if (valid.startsWith(SerialMessage.Acknowledge)) {
                        int ackId = Character.getNumericValue(valid.charAt(valid.length() - 1));
                        message = createAcknowledge(ackId);
                    }
                    // Message (ex: &[M]P:I:I$)
                    if (valid.startsWith(SerialMessage.Message)) {

                        valid = valid.replaceFirst("\\[M\\]", "");
                        int nextSeparator = valid.indexOf(SerialMessage.Separator);
                        String sender = valid.substring(0, nextSeparator);

                        valid = valid.substring(nextSeparator+1, valid.length());
                        nextSeparator = valid.indexOf(SerialMessage.Separator);
                        String com = valid.substring(0, nextSeparator);

                        valid = valid.substring(nextSeparator+1, valid.length());
                        String mes = valid;

                        message = createReceived(SerialMessage.Message, sender, com, mes);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return message;
    }
}
