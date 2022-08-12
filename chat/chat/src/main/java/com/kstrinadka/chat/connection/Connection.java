package com.kstrinadka.chat.connection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection implements Closeable {

    //TODO создать ObjectReader и ObjectWriter
    // добавить прием, отправку JSON и Message

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    private boolean isClosedFlag = false;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream(), StandardCharsets.UTF_8));
    }


    /**
     * Метод отправляет сообщение через сокет
     */
    public void send(String simpleMessage) throws IOException {
        synchronized (out) {
            out.write(simpleMessage);
            out.newLine();
            out.flush();
        }
    }

    public boolean isClosed() {
        return isClosedFlag;
    }

    /**
     * Метод принимает сообщение через сокет
     * @return Возвращает полученное сообщение
     */
    public String receive() throws IOException {
        synchronized (in) {
            String message = in.readLine();
            return message;
        }
    }


    @Override
    public void close() {
        isClosedFlag = true;
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
