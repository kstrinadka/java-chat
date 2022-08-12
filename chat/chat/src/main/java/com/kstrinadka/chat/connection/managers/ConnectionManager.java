package com.kstrinadka.chat.connection.managers;


import com.kstrinadka.chat.connection.Connection;
import javafx.scene.shape.StrokeLineCap;

import java.io.IOException;
import java.util.*;

/**
 * Это обертка над Connection, которая нужна, чтобы сервер мог управлять соединеним (писать, читать в него, ...)
 *
 * Будет создаваться из ServerSocket.accept()
 *
 * Именно эта штука будет в пуле потоков сервера
 *
 *
 */
public abstract class ConnectionManager implements Runnable {


    public final String clientUserName;
    protected final Connection connection;

    //Списоk клиентов с их соединениями (чтобы можно было послать сообщение каждому клиенту)
    protected final List<ConnectionManager> allUsers;

    //Списоk клиентов с их соединениями (чтобы можно было послать сообщение каждому клиенту)
    //private final Map<String, ConnectionManager> allUsers = new HashMap<>();


    public ConnectionManager(Connection connection, List<ConnectionManager> allUsers) {
        this.connection = connection;
        this.allUsers = allUsers;
        this.clientUserName = registrationClientName();
        if (this.clientUserName != null) {
            allUsers.add(this);
            broadcastMessage("Server: " + clientUserName + " has entered the chat! ");
        }
        else {
            System.out.println("can't get client name");
        }
    }


    /**
     * Будет вызываться при добавлении и удалении клиента
     * @param allUsersNames - обновленный список клиентов
     */
    abstract public void updateChatState(List<String> allUsersNames);

    /**
     * Метод запрашивает имя у клиента и добавляет его в мапу
     * @param connection объект соединения
     * @return возвращает никнейм клиента
     */
    abstract public String addClient(Connection connection);



    abstract void tryToAddClient(Map<String, ConnectionManager> allUsers);

    abstract public String registrationClientName();

    abstract public void sendToOneConnection(String message);


    /**
     * Метод реализует обмен сообщениями между клиентами
     * @param connection объект соединения
     * @param userName никнейм текущего клиента
     */
    abstract public void messagingBetweenUsers(Connection connection, String userName);


    abstract public void broadcastMessage(String messageToSend);

    abstract public void removeConnectionManager();


    abstract public void closeEverything();

}
