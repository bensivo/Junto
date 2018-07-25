package main.networking.core;

import main.networking.interfaces.SocketAcceptor;
import main.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Used by the server to connect to multiple clients.
 * A thread that listens for incoming connections on the port given. Sockets are forwarded to a
 * SocketAcceptor for further handling, while the ConnectionPooler continues listening for new sockets.
 */
public class ConnectionPooler implements Runnable{
    private int port;
    private ServerSocket server;
    private SocketAcceptor socketAcceptor = null;

    public ConnectionPooler(int port) {
        this.port = port;
    }

    /**
     * This thread is what accepts new connections, and spawns new DataPacketReceiver threads
     */
    public void run(){
        try {
            server = new ServerSocket(port);
            Logger.logI("ConnectionPooler", "Server started");
            Logger.logI("ConnectionPooler", "Using port " + port);

            //This loop continually accepts new clients and starts new threads for them
            while(!Thread.currentThread().isInterrupted()){
                Logger.logI("ConnectionPooler", "Looking for new client");
                Socket socket = server.accept();
                if(socketAcceptor != null){
                    socketAcceptor.onNewSocketConnected(socket);
                }
            }
        }catch(IOException ioe){
            Logger.logE("ConnectionPooler","Server Socket Closed");
        }
    }

    /**
     * Stop both the listening server socket
     *
     * Note: Closing a Socket with this function will often throw a SocketException. This exception is not
     *       fatal to the program, and nothing will be harmed by it.
     */
    public void instantStop(){
        try{
            server.close();
            Thread.currentThread().interrupt();

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void setSocketAcceptor(SocketAcceptor socketAcceptor){
        this.socketAcceptor = socketAcceptor;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
