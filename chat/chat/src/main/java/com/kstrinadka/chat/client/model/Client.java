package com.kstrinadka.chat.client.model;

import com.kstrinadka.chat.connection.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private final String IP_ADDR;
    private final int PORT;
    //private final Socket socket;
    private final Connection connection;

    private boolean chatingState = true;

    private BufferedReader consoleReader;



    public Client(String IP_ADDR, int PORT) {
        this.IP_ADDR = IP_ADDR;
        this.PORT = PORT;
        this.connection = connectToServer();
        if (this.connection != null) {
            startChating();
        }
        else {
            System.out.println("can't create connection to server");
        }
    }


    public Connection connectToServer() {
        Connection connectionTest = null;
        try {
            Socket socket = new Socket(IP_ADDR, PORT);
            connectionTest = new Connection(socket);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
        }
        catch (IOException e) {
            System.out.println("can't create client socket");
        }
        return connectionTest;
    }

    private void startChating() {
        sendUserName();

        try {
            while (chatingState) {
                System.out.print("> ");
                Thread listenerFromserver = new Thread(new ListenerThread());
                listenerFromserver.start();

                String message = consoleReader.readLine();
                if (messageIsSpecialCommand(message)) {
                    System.out.println("this is special command");
                }
                else if (!message.equalsIgnoreCase("") && message != null) {
                    connection.send(message);
                }


                /*String inputMessage = connection.receive();
                if (inputMessage != null && !inputMessage.equalsIgnoreCase("")) {
                    System.out.println("from server: " + inputMessage);
                }*/


            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("error in chating");
            connection.close();
        }


    }


    public boolean messageIsSpecialCommand(String message) {
        try {
            if (message.equalsIgnoreCase("quit")) {
                connection.send(message);
                chatingState = false;
                return true;
            }
        }
        catch (IOException e) {
            System.out.println("error in special command");
            e.printStackTrace();
        }


        return false;
    }

    private void sendUserName() {
        try {
            String message = consoleReader.readLine();
            connection.send(message);
        }
        catch (IOException e) {
            System.out.println("can't read client name from console");
        }
    }

    public void closeResources(){
        try {
            if (consoleReader != null) {
                consoleReader.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        catch (IOException e) {
            System.out.println("can't close resources");
            e.printStackTrace();
        }
    }


    class ListenerThread implements Runnable {
        @Override
        public void run() {
            while(chatingState) {
                try {
                    // read the message form the input stream
                    String msg = connection.receive();

                    // print the message
                    if (msg != null && !msg.equalsIgnoreCase("")) {
                        System.out.println(msg);
                        System.out.print("> ");
                    }

                }
                catch(IOException e) {
                    System.out.println("Server has closed the connection: " + e);
                    break;
                }
            }
        }
    }




    public static void main(String[] args) {

        Client client = new Client("127.0.0.1", 9999);


    }





}
