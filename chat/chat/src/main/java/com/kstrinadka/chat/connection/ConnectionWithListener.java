package com.kstrinadka.chat.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * Конкретное реализованное соединение между клиентом и сервером
 *
 * Содержит: сокет, входящий поток, исходящий поток.
 *
 * Для полноценного взаимодейсвия удобно сделать обертку над этим соединением, которая будет полем Сервера.
 * Эту обертку можно будет запускать в отдельном потоке.
 *
 * TODO сделать потоки ввода и вывода
 */
public class ConnectionWithListener implements Runnable
{

    private final Socket clientSocket;

    private final BufferedReader readerFromSocket;
    private final BufferedWriter writerToSocket;

    private final ConnectionListener eventListener;


    public ConnectionWithListener(ServerSocket serverSocket, ConnectionListener eventListener) throws IOException {
        this.clientSocket = serverSocket.accept();
        this.eventListener = eventListener;
        readerFromSocket = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream(), StandardCharsets.UTF_8));
        writerToSocket = new BufferedWriter(new OutputStreamWriter(
                clientSocket.getOutputStream(), StandardCharsets.UTF_8));
    }


    public ConnectionWithListener(String ipAddr, int PORT, ConnectionListener eventListener) throws IOException {
        this(new Socket(ipAddr, PORT), eventListener);
    }


    public ConnectionWithListener(Socket clientSocket, ConnectionListener eventListener) throws IOException {
        this.eventListener = eventListener;
        this.clientSocket = clientSocket;
        readerFromSocket = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream(), StandardCharsets.UTF_8));
        writerToSocket = new BufferedWriter(new OutputStreamWriter(
                clientSocket.getOutputStream(), StandardCharsets.UTF_8));
    }


    public void close() throws IOException {
        readerFromSocket.close();
        writerToSocket.close();
        clientSocket.close();
    }


    public void sendString(String stringToSend) throws IOException {
        writerToSocket.write(stringToSend);
        writerToSocket.newLine();
        writerToSocket.flush();
    }


    public String readString() throws IOException {
        String readString = readerFromSocket.readLine();
        return readString;
    }

    @Override
    public void run() {

        while (true) {

            try {
                String message = readerFromSocket.readLine();

                if (message.equalsIgnoreCase("quit")) {
                    break;
                }

                eventListener.onReceiveString(ConnectionWithListener.this, message);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void messaging() {

    }
}
