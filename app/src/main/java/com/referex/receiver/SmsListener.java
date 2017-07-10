package com.referex.receiver;

public interface SmsListener {
    public void messageReceived (String messageText, int message_type);
}
