package main.networking.interfaces;

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
    void broadcast(Object obj);
    //void handleLocalDiff(Diff localDiff); //LEGACY, to be deprecated
    void attachListener(NetworkManagerListener l);
}
