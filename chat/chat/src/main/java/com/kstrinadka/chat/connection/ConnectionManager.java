package com.kstrinadka.chat.connection;


/**
 * Это обертка над Connection, которая нужна, чтобы сервер мог управлять соединеним (писать, читать в него, ...)
 *
 * Будет создаваться из ServerSocket.accept()
 *
 *
 */
public class ConnectionManager implements Runnable {

    private final ConnectionWithListener connectionWithListener;


}
