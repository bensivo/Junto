package main.networking;

import main.networking.ClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Listens for incoming connections on the given port
 * and start new ClientConnection Threads for each connection.
 *
 * Allows the Server to handle multiple clients simultaneously
 *
 * @author Benjamin
 */
public class ClientPooler implements Runnable{
    private int port;
    private ServerSocket server;
    private ArrayList<ClientConnection> clientConnections = new ArrayList<>();
    private ClientConnection.ClientConnectionListener listener = null;

    public ClientPooler(int port){
        this.port = port;
    }

    public ArrayList<ClientConnection> getClientConnections(){
        return clientConnections;
    }


    public void registerListener(ClientConnection.ClientConnectionListener listener){
        this.listener = listener;
        for(ClientConnection connection: this.clientConnections){
            connection.registerListener(listener);
        }
    }

    /**
     * Stop both the listening server socket, and all client connections
     *
     * Note: Closing a Socket with this function will often throw a SocketException. This exception is not
     *       fatal to the program, and nothing will be harmed by it.
     */
    public void instantStop(){
        try{
            server.close();
            Thread.currentThread().interrupt();

            for(ClientConnection child: clientConnections){
                child.instantStop();
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Starts the server listener thread.
     * Called on Thread.execute()
     */
    public void run(){
        try {
            System.out.println("Using port " + port);
            server = new ServerSocket(port);
            System.out.println("Server started");

            //This loop continually accepts new clients and starts new threads for them
            while(!Thread.currentThread().isInterrupted()){
                System.out.println("Looking for new client");
                Socket socket = server.accept();

                //Create a new client connection with this socket, and create a thread to run it on
                ClientConnection connection = new ClientConnection(socket);
                if(this.listener != null){
                     connection.registerListener(this.listener);
                }
                Thread thread = new Thread(connection);
                clientConnections.add(connection);
                thread.start();
            }

        }catch(IOException ioe){
            System.out.println("Server Socket Closed");
        }


    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
