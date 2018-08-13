package main.networking;

import javafx.scene.control.Alert;

import java.io.*;
import java.net.*;

/**
 * Connects to a server running on the same network.
 * Once the connection is established, DataPackets can be sent
 * to the Server
 */
public class Client extends NetworkManager{

    public Client(DataPacketRouter router){
        super(router);
    }

    @Override
    public void start() {
        try{
            this.connect("127.0.0.1", 5001);
        }
        catch(SocketTimeoutException ste){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SOCKET ERROR");
            alert.setHeaderText("Socket timeout");
            alert.setContentText("Connection timeout occured with attempting to create a socket to the server endpoint");
            alert.showAndWait();
        }
    }

    @Override
    public void stop() {
        for(DataOutputStream stream: this.outputStreams){
            try{
                stream.close();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }

        for(Thread t: this.runningThreads){
            t.interrupt();
        }
    }

    /**
     * Attempt to establish a connection to another Junto application
     * @param ip Hostname of the machine to make the connection to
     * @param port Port to use for the socket connection
     */
    private void connect(String ip, int port) throws SocketTimeoutException{
        try{
            Socket newSocket = new Socket();
            newSocket.connect(new InetSocketAddress(ip, port), 3000); // Timeout of 3 seconds

            onNewSocketConnected(newSocket);

            System.out.println("Connected to server at ip:" + ip + ":"+port);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
