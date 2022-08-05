package com.kstrinadka.chat.server.model;


import com.kstrinadka.chat.connection.ConnectionWithListener;
import com.kstrinadka.chat.connection.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/*
TODO - Унаследовать от серевера 2 вида серверов: xml и с Сериализацией

 */
public class Server implements ConnectionListener {

    public static void main(String[] args) {

        Server server = new Server(9999);

    }

    ServerSocket serverSocket;

    ExecutorService threadPool;

    int PORT;

    //Список соединений
    private final ArrayList<ConnectionWithListener> connectionWithListeners = new ArrayList<>();

    private final ConnectionWithListener testConnectionWithListener;


    //Тут еще нужно будет пул потоков (и сами потоки) как-то создавать
    public Server(int PORT){

        this.PORT = PORT;

        System.out.println("Server running...");

        //слушает порт и принимает входящее соединение
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Thread connectionThread = new Thread( new ConnectionWithListener(serverSocket.accept(), this));
                    connectionThread.start();
                }
                catch (IOException e) {
                    System.out.println("Connection exception: " + e);

                    //TODO close all conections
                    // break cycle
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO может сделать ClientTask, которая Runnable, которая создается на соединении
        //TODO и ее нужно передавать в поток, чтобы оно там исполнялось

    }


    public synchronized void onConnectionReady (ConnectionWithListener connectionWithListener) {
        connectionWithListeners.add(connectionWithListener);
        sendToAllConnections("Client connected: " + connectionWithListener);
    }

    @Override
    public void onReceiveString(ConnectionWithListener connectionWithListener, String value) {
        sendToAllConnections(value);
    }

    @Override
    public void onDisconnect(ConnectionWithListener connectionWithListener) {
        connectionWithListeners.remove(connectionWithListener);
        sendToAllConnections("Client disconnected: " + connectionWithListener);
    }

    @Override
    public void onException(ConnectionWithListener connectionWithListener, Exception e) {
        System.out.println("Connection exception: " + e);
    }

    public void sendToAllConnections(String message) {
        System.out.println(message);
        for (int i = 0; i < connectionWithListeners.size(); ++i) {
            try {
                connectionWithListeners.get(i).sendString(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void disconnectClient(ConnectionWithListener connectionWithListener) {

    }


    public List<String> showChatMembers(ConnectionWithListener connectionWithListener) {
        return null;
    }

    public static void runServerOnPort(int PORT) throws IOException {
        Server server = new Server(PORT);



    }

}
