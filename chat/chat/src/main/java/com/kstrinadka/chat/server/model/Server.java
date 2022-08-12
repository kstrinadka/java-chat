package com.kstrinadka.chat.server.model;


import com.kstrinadka.chat.connection.Connection;
import com.kstrinadka.chat.connection.ConnectionWithListener;
import com.kstrinadka.chat.connection.ConnectionListener;
import com.kstrinadka.chat.connection.managers.ConnectionManager;
import com.kstrinadka.chat.connection.managers.SimpleConnectionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/*
TODO - Унаследовать от серевера 2 вида серверов: xml и с Сериализацией

 */
public class Server{



    ServerSocket serverSocket;
    ExecutorService threadPool;

    //Списоk клиентов с их соединениями
    //private final Map<String, ConnectionManager> allUsers = new HashMap<>();
    private final List<String> allUsersNames = new ArrayList<>();

    protected final List<ConnectionManager> allUsers = new ArrayList<>();



    public Server() {
        this(9999);
    }


    //Тут еще нужно будет пул потоков (и сами потоки) как-то создавать
    public Server(int PORT){

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running...");
            while (true) {
                try {

                    Connection connection = new Connection(serverSocket.accept());
                    ConnectionManager connectionManager = new SimpleConnectionManager(connection, this.allUsers);

                    System.out.println("added new user");
                    writeUsersList();


                    //пока что без пула потоков
                    Thread connectionThread = new Thread(connectionManager);
                    connectionThread.start();
                }
                catch (IOException e) {
                    System.out.println("Connection exception: " + e);
                    break;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }


    public void writeUsersList() {
        System.out.println("users list:");
        for (ConnectionManager manager: allUsers) {
            System.out.println(manager.clientUserName);
        }
        System.out.println("");
    }



    private void sendMessageToAllUsers() {

    }



    public static void main(String[] args) {

        Server server = new Server(9999);

    }
}
