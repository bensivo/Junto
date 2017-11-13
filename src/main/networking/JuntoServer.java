package main.networking;

/**
 * Opens a web socket on a specific port
 * Clients connect to the port, and send strings through the generated socket
 * All strings are printed to the screen
 */
public class JuntoServer {
    private int port;
    private ClientPooler clientPooler;

    public JuntoServer(){
        this(5001);
    }

    public JuntoServer(int port) {
        this.port = port;
    }


    public void setPort(int p){
        this.port = p;
    }

    public int getPort(){
        return this.port;
    }

    public void start(){
        clientPooler = new ClientPooler(this.port);
        Thread serverThread = new Thread(clientPooler);
        serverThread.start();
    }

    public void stop(){
        clientPooler.instantStop();
    }
}
