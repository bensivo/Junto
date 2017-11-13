package main.networking;

/**
 * Opens a web socket on a specific port
 * Clients connect to the port, and send strings through the generated socket
 * All strings are printed to the screen
 */
public class JuntoServer {
    private int port;
    private ClientPooler clientPooler;
    private ClientConnection.ClientConnectionListener listener = null;

    public JuntoServer(){
        this(5001, null);
    }

    public JuntoServer(ClientConnection.ClientConnectionListener listener){
        this(5001, listener);
    }

    public JuntoServer(int port, ClientConnection.ClientConnectionListener listener) {
        this.listener = listener;
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
        if(this.listener != null){
            clientPooler.registerListener(this.listener);
        }

        Thread serverThread = new Thread(clientPooler);
        serverThread.start();
    }

    public void stop(){
        clientPooler.instantStop();
    }

    public void registerListener(ClientConnection.ClientConnectionListener listener){
        this.listener = listener;
        clientPooler.registerListener(listener);
    }
}
