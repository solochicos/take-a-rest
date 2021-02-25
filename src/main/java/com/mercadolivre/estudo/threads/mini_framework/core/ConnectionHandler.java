package com.mercadolivre.estudo.threads.mini_framework.core;

import com.mercadolivre.estudo.threads.mini_framework.utils.Logger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ConnectionHandler implements Callable<String> {

    private Socket client;

    public ConnectionHandler(Socket client) {
        this.client = client;
    }

    @Override
    public String call() throws Exception {
        Logger.log("Handling connection for " + client.getInetAddress().getHostAddress() + ":" + client.getPort(), "Connection Handler");

        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try {
            Logger.log("Reading input data", "Connection Handler");
            inputStream = new ObjectInputStream(client.getInputStream());
            String inputData = (String) inputStream.readObject();

            Logger.log("Readed data ["+inputData+"]", "Connection Handler");

            String output = inputData + "verified";

            Logger.log("Writing output data", "Connection Handler");
            outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.writeObject(output);
        } finally {
            outputStream.close();
            inputStream.close();
            client.close();
        }
        return null;
    }
}
