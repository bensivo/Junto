package main.networking.interfaces;

import java.net.Socket;

/**
 * For classes that wish to be notified of incoming socket connections that are received in the Connection Pooler
 */
public interface ConnectionPoolerListener {
    void onNewSocketConnected(Socket socket);
}
