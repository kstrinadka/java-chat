package com.kstrinadka.chat.connection.managers;


import com.kstrinadka.chat.connection.Connection;
import com.kstrinadka.chat.connection.managers.ConnectionManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SimpleConnectionManager extends ConnectionManager {


    public SimpleConnectionManager(Connection connection, List<ConnectionManager> allUsers) {
        super(connection, allUsers);
    }

    @Override
    public void updateChatState(List<String> allUsersNames) {

    }

    @Override
    public String addClient(Connection connection) {
        return null;
    }

    @Override
    void tryToAddClient(Map<String, ConnectionManager> allUsers) {

    }

    @Override
    public String registrationClientName() {
        String name = null;
        try {
            name = connection.receive();
        }
        catch (IOException e) {
            System.out.println("can't get client name");
            closeEverything();
        }

        return name;
    }


    @Override
    public void messagingBetweenUsers(Connection connection, String userName) {

    }

    public void sendToOneConnection(String message) {
        try {
            connection.send(message);
        }
        catch (IOException e) {

        }

    }


    @Override
    public void broadcastMessage(String messageToSend) {
        System.out.println("broadcast message...");
        for (ConnectionManager manager : allUsers) {
            if (!manager.clientUserName.equals(this.clientUserName)) {
                manager.sendToOneConnection(messageToSend);

            }
        }
    }

    @Override
    public void removeConnectionManager() {
        broadcastMessage("SERVER: " + clientUserName + " has left the chat!");
        allUsers.remove(this);
    }


    @Override
    public void closeEverything() {
        removeConnectionManager();
        connection.close();
    }


    public void handleMessage(String message) {
        if (message.equalsIgnoreCase("quit")) {
            System.out.println("try to disconnect: " + clientUserName);
            closeEverything();

        }
        else if (message.equals("/list")) {
            this.sendListOfUsers();
        }
        else {
            broadcastMessage(clientUserName + ": " + message);
        }
    }

    private void sendListOfUsers() {
        String list = "";
        for (ConnectionManager manager: allUsers) {
            list += manager.clientUserName;
            list+= "\n";
        }
        sendToOneConnection(list);
    }

    @Override
    public void run() {
        System.out.println("Thread for client " + this.clientUserName + " created!");
        String messageFromClient;

        while (!connection.isClosed()) {
            try {
                messageFromClient = connection.receive();

                if (messageFromClient != null) {
                    handleMessage(messageFromClient);
                }
                else {
                    System.out.println("messageFromClient == null");
                }



            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("error in Thread loop");
                connection.close();
                break;
            }
        }
    }


}
