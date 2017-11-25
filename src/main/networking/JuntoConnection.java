package main.networking;

import main.networking.interfaces.NetworkManagerListener;
import main.networking.interfaces.NetworkManager;
import main.util.Diff;
import main.util.Logger;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Manages a single connection between 2 Junto applications
 * This connection is the entry point of external DataPackets, and the exit point of outgoing DataPackets
 */
public class JuntoConnection {
    private static final int PORT = 5001;

    NetworkManager networkManager = null;

    Collection<Socket> sockets;
    Collection<Thread> receiverThreads;

    NetworkManagerListener connectionListener = null; //callbacks

    ConnectionPooler pooler = null; //Runnable that listens for new socket connections
    Thread poolerThread;

    public JuntoConnection(){
        this.connectionListener = null;
        this.sockets = new ArrayList<>();
        this.receiverThreads = new ArrayList<>();
    }

    public void attach(NetworkManagerListener listener){
        this.connectionListener = listener;
    }

    public void start(){
        networkManager.start();
    }


    public void handleLocalDiff(Diff diff){
        networkManager.handleLocalDiff(diff);
    }

    public void switchNetworkManager(int type){
        switch(type){
            case NetworkManager.TYPE_CLIENT:
                networkManager = new Client();
                break;
            case NetworkManager.TYPE_SERVER:
                networkManager = new Server();
                break;
        }
        networkManager.attachListener(this.connectionListener);
    }

}
