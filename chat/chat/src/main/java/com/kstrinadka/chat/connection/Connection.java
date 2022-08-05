package com.kstrinadka.chat.connection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection implements Closeable {

    //TODO создать ObjectReader и ObjectWriter

    private final Socket socket;
    private final BufferedReader readerFromSocket;
    private final BufferedWriter writerToSocket;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        readerFromSocket = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), StandardCharsets.UTF_8));
        writerToSocket = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream(), StandardCharsets.UTF_8));
    }




}
