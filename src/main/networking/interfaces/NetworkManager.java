package main.networking.interfaces;

import main.DataPacket;
import main.util.Diff;

public interface NetworkManager {
    int TYPE_CLIENT = 1;
    int TYPE_SERVER = 2;

    void start();
    void stop();
    void handleLocalDiff(Diff localDiff);
    void attachListener(NetworkManagerListener l);
}
