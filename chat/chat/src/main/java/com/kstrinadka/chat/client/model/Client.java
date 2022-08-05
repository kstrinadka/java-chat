package com.kstrinadka.chat.client.model;

import com.kstrinadka.chat.connection.ConnectionWithListener;
import com.kstrinadka.chat.connection.ConnectionListener;

import java.io.IOException;

public class Client implements ConnectionListener {
    private final String IP_ADDR = "127.0.0.1";
    private final int PORT = 9999;


    public static void main(String[] args) {

        Client client = new Client();


    }


    private ConnectionWithListener connectionWithListener;



    private Client() {

        //тут была настройка графического интерфейса

        try {
            connectionWithListener = new ConnectionWithListener(IP_ADDR, PORT, this);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }


    /**
     * Это обработка нажатия на кнопку. Отправляется ник + сообщение
     */
    public void actionPerformed() {

        /*String msg = fieldInput.getText();
        if (msg.equals("")) return;

        fieldInput.setText(null);
        connection.sendString(fieldNickName.getText() + ": " + msg);*/

    }


    @Override
    public void onConnectionReady(ConnectionWithListener tcpConnectionWithListener) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(ConnectionWithListener connectionWithListener, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(ConnectionWithListener connectionWithListener) {
        printMsg("Connection closed");
    }

    @Override
    public void onException(ConnectionWithListener connectionWithListener, Exception e) {
        printMsg("Connection exception: " + e);
    }


    /**
     * Выводит сообщение от сервера на экран
     */
    private synchronized void printMsg (String msg) {
        System.out.println(msg);
    }
}
