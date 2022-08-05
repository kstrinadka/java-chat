package com.kstrinadka.chat.connection;


/**
 * Вместо этой штуки можно подсовывать разные eventListener'ы, чтобы по-разному реагировать на события
 * В качестве слушателей будут выступать сервер и клиенты
 */
public interface ConnectionListener {

    void onConnectionReady(ConnectionWithListener connectionWithListener);
    void onReceiveString (ConnectionWithListener connectionWithListener, String value);
    void onDisconnect (ConnectionWithListener connectionWithListener);
    void onException (ConnectionWithListener connectionWithListener, Exception e);

}
