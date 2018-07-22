package main.networking.interfaces;

import main.optransform.Diff;

/**
 * A Netowork Manager defines any class that has CONTROL over the main
 * networking operations of the app, such as network capabilities on and off.
 * Network managers must also define callbacks for recieved information.
 */
public interface NetworkManager {
    int TYPE_CLIENT = 1;
    int TYPE_SERVER = 2;

    void start();
    void stop();
    void handleLocalDiff(Diff localDiff);
    void attachListener(NetworkManagerListener l);
}
