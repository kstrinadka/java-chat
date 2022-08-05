package com.kstrinadka.chat.message;

import java.io.Serializable;

public class StringMessage extends AbstractMessage {

    String textOfMessage;

    public StringMessage(String textOfMessage) {
        this.textOfMessage = textOfMessage;
    }

}
