package main.networking;

import javafx.scene.control.Alert;
import main.networking.core.DataPacket;
import main.networking.core.DataPacketReceiver;
import main.networking.interfaces.DataPacketReceiverListener;
import main.networking.interfaces.NetworkManager;
import main.networking.interfaces.NetworkManagerListener;
import main.optransform.Operation;
import main.util.ByteUtils;
import main.util.Logger;

import java.io.*;
import java.net.*;

/**
 * Connects to a server running on the same network.
 * Once the connection is established, DataPackets can be sent
 * to the Server
 */
public class Client implements NetworkManager, DataPacketReceiverListener {

    private String serverName;
    private int serverPort;
    private Socket socket = null;
    private DataOutputStream streamOut;
    private DataPacketReceiver receiver = null;
    private NetworkManagerListener listener = null;

    /**
     * Creates a socket to the given server on the given port, and starts listening to System.in
     * For user input to send to the server.
     * User input is continually sent to the server until ".bye" is sent
     */
    public Client() { }

    /**
     * Attempt to establish a connection to another Junto application
     * @param ip Hostname of the machine to make the connection to
     * @param port Port to use for the socket connection
     */
    public void connect(String ip, int port) throws SocketTimeoutException{
        try{
            Socket newSocket = new Socket();
            newSocket.connect(new InetSocketAddress(ip, port), 3000);

            this.receiver = new DataPacketReceiver(newSocket, this);
            Thread t = new Thread(receiver);
            t.start();
            this.socket= newSocket;
            this.streamOut = new DataOutputStream(new BufferedOutputStream(newSocket.getOutputStream()));

            System.out.println("Connected to server at ip:" + ip + ":"+port);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Convert a DataPacket Object to an array of bytes (with a length packet appended
     * at the front), and send it across the socket
     * @param dataPacket The DataPacket to send
     */
    public void sendPacket(DataPacket dataPacket){
        try {
            byte[] bytes = ByteUtils.toBytesWithLength(dataPacket);
            streamOut.write(bytes);
            streamOut.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
        try{
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void attachListener(NetworkManagerListener listener){
        this.listener = listener;
    }


    @Override
    public void onDataPacketReceived(DataPacket dataPacket, DataPacketReceiver source) {
        Logger.logI("CLIENT", "New DataPacket received");
        /**
         * TODO: Push triage to another class. Use the dispatcher, subscriber pattern
         */
        switch (dataPacket.getType()){
            case DataPacket.TYPE_OP:
                Logger.logI("CLIENT", "of type Operation");
                if(listener != null){
                    try{
                        listener.onOperationRecieved((Operation) ByteUtils.fromBytes(dataPacket.getData()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void broadcast(Object obj){
        try{
            if(streamOut != null){
                DataPacket dataPacket = new DataPacket("client", "server", DataPacket.TYPE_OP, ByteUtils.toBytes(obj));
                streamOut.write(ByteUtils.toBytesWithLength(dataPacket));
                streamOut.flush();
            }
        }catch (IOException ioe){
            Logger.logI("CLIENT", "IOError in broadcast");
            ioe.printStackTrace();
        }
    }
}
