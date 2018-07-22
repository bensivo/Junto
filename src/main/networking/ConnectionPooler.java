package main.networking;

import main.networking.interfaces.ConnectionPoolerListener;
import main.networking.interfaces.DataPacketReceiverListener;
import main.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Listens for incoming connections on the given port
 * and start new DataPacketReceiver Threads for each connection.
 *
 * Allows the Server to handle multiple clients simultaneously
 *
 * @author Benjamin
 */
public class ConnectionPooler implements Runnable{

    private int port;
    private ServerSocket server;

    //Handler for  incoming data packets
    private DataPacketReceiverListener receiverListener = null;
    //Handler for local ConnectionPooler events
    private ConnectionPoolerListener poolerListener = null;

    //A list of DataPacketReceivers, each running on its own thread, connected to its own socket
    private ArrayList<DataPacketReceiver> dataPacketReceivers = new ArrayList<>();

    public ConnectionPooler(int port) {
        this.port = port;
    }

    /**
     * This thread is what accepts new connections, and spawns new DataPacketReceiver threads
     */
    public void run(){
        try {
            Logger.logI("ConnectionPooler", "Using port " + port);
            server = new ServerSocket(port);
            Logger.logI("ConnectionPooler", "Server started");

            //This loop continually accepts new clients and starts new threads for them
            while(!Thread.currentThread().isInterrupted()){
                Logger.logI("ConnectionPooler", "Looking for new client");
                Socket socket = server.accept();
                if(poolerListener != null){
                    poolerListener.onNewSocketConnected(socket);
                }

            }

        }catch(IOException ioe){
            Logger.logE("ConnectionPooler","Server Socket Closed");
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

            for(DataPacketReceiver child: dataPacketReceivers){
                child.instantStop();
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public ArrayList<DataPacketReceiver> getDataPacketReceivers(){
        return dataPacketReceivers;
    }

    public void registerListeners(ConnectionPoolerListener poolerListener, DataPacketReceiverListener receiverListener){
        this.poolerListener = poolerListener;
        this.receiverListener = receiverListener;
        for(DataPacketReceiver connection: this.dataPacketReceivers){
            connection.attach(this.receiverListener);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
