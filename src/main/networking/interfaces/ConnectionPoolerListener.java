package main.networking.interfaces;

import java.net.Socket;

public interface ConnectionPoolerListener {
    void onNewSocketConnected(Socket socket);
}
