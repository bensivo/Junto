package main.networking;

import main.networking.interfaces.ConnectionPoolerListener;
import main.networking.interfaces.DataPacketReceiverListener;
import main.networking.interfaces.NetworkManager;
import main.networking.interfaces.NetworkManagerListener;
import main.util.ByteUtils;
import main.optransform.Diff;
import main.util.Logger;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to listen on a specific port for Junto Client applications to connect to.
 */
public class Server implements NetworkManager, ConnectionPoolerListener, DataPacketReceiverListener {
    private static final int PORT = 5001;

    List<DataPacketReceiver> receivers;

    ConnectionPooler pooler = null; //Runnable that listens for new socket connections
    Thread poolerThread;
    NetworkManagerListener listener = null;

    public Server(){
        this.receivers = new ArrayList<>();
        this.pooler = new ConnectionPooler(PORT);

        pooler.registerListeners(this, this);
        Logger.logI("SERVER", "New Server Created. Pooler created and registered");
    }


    @Override //NetworkManager
    public void start() {
        this.listen(PORT);
    }

    @Override //NetworkManager
    public void stop() {
        poolerThread.interrupt();
    }

    @Override //NetworkManager
    public void handleLocalDiff(Diff localDiff) {
        Logger.logI("SERVER", "handleLocalDiff");
        for(DataPacketReceiver receiver: receivers){
            Socket socket = receiver.getSocket();
            try{
                /**
                 * TODO: Let's not create a new outputstream anytime we try to send a datapacket
                 */
                OutputStream streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                DataPacket dp = new DataPacket("Server", "Client", DataPacket.TYPE_DIFF, ByteUtils.toBytes(localDiff));
                streamOut.write(ByteUtils.toBytesWithLength(dp));
                streamOut.flush();
            }
            catch(Exception e){
                Logger.logE("SERVER", "Handle local diff failed on socket: " + socket.getInetAddress().toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * Called when a new Junto application initiates a connection, creating a socket.
     * Creates a thread to listen for dataPackets on teh new socket, and adds the socket to the local list of open sockets
     * @param socket the newly connected socket
     */
    @Override //ConnectionPoolerListener
    public void onNewSocketConnected(Socket socket){
        Logger.logE("SERVER", "New Socket Connected");
        DataPacketReceiver receiver = new DataPacketReceiver(socket, this);
        this.receivers.add(receiver);
        Thread t = new Thread(receiver);
        t.start();
    }

    /**
     *
     * @param dataPacket
     */
    @Override //DataPacketReceiverListener
    public void onDataPacketReceived(DataPacket dataPacket, DataPacketReceiver receiver) {
        Logger.logI("SERVER", "DataPacket received from: " + dataPacket.getSource());
        if(dataPacket.getType() == DataPacket.TYPE_DIFF){
            Logger.logI("SERVER", "OF type diff");
            if(listener != null){
                try{
                    Diff diff = (Diff) ByteUtils.fromBytes(dataPacket.getData());
                    listener.onDiffPacketReceived(diff);
                    this.routeDiff(diff, receiver);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Send the given diff to all sockets, except for the one on the given thread
     * @param diff The Diff object to route to all the other clients
     * @param sourceReceiver The Receiver that the diff came from. No diff will be sent to this one
     */
    private void routeDiff(Diff diff, DataPacketReceiver sourceReceiver){
        for(DataPacketReceiver receiver: receivers){
            if(receiver != sourceReceiver){
                Socket socket = receiver.getSocket();
                try{
                    OutputStream streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    DataPacket dp = new DataPacket("Server", "Client", DataPacket.TYPE_DIFF, ByteUtils.toBytes(diff));
                    streamOut.write(ByteUtils.toBytesWithLength(dp));
                    streamOut.flush();
                }
                catch(Exception e){
                    Logger.logE("SERVER", "Handle local diff failed on socket: " + socket.getInetAddress().toString());
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public void attachListener(NetworkManagerListener listener){
        this.listener = listener;
    }

    /**
     * Starts the ConnectionPooler thread that begins accepting new socket connections on the given port
     * @param port The port to listen for incoming connections on
     */
    private void listen(int port) {
        poolerThread = new Thread(pooler);
        poolerThread.start();
        System.out.println("Listening on port: " + PORT);

    }
}
