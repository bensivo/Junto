package main.networking;

import main.networking.core.ConnectionPooler;
import main.networking.core.DataPacket;
import main.networking.core.DataPacketReceiver;
import main.networking.interfaces.ConnectionPoolerListener;
import main.networking.interfaces.DataPacketReceiverListener;
import main.networking.interfaces.NetworkManager;
import main.networking.interfaces.NetworkManagerListener;
import main.optransform.Operation;
import main.util.ByteUtils;
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

    private ConnectionPooler pooler = null; //Runnable that listens for new socket connections
    private Thread poolerThread;
    private List<DataPacketReceiver> receivers;

    private NetworkManagerListener listener = null;


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

    /**
     * Starts the ConnectionPooler thread that begins accepting new socket connections on the given port
     * @param port The port to listen for incoming connections on
     */
    private void listen(int port) {
        poolerThread = new Thread(pooler);
        poolerThread.start();
        System.out.println("Listening on port: " + PORT);

    }

    /**
     * Called when a new client connects to this server's Connection Pooler
     * Creates a DataPackerReciever(Runnable / Thread) to listen for Packets on that connection, and registers this server as the main callback for that receiver.
     * @see(onDataPacketRecieved)
     *
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
     * What to do when a datapacket comes in from one of the DataPacketReceiver threads
     * @param dataPacket
     */
    @Override //DataPacketReceiverListener
    public void onDataPacketReceived(DataPacket dataPacket, DataPacketReceiver receiver) {
        Logger.logI("SERVER", "DataPacket received from: " + dataPacket.getSource());

        /**
         * TODO: Push routing to another class (use the emitter and subscriber pattern)
         */
        /**
         * TODO: Implement operation forwarding (forwarding to all other clients)
         */
        if(dataPacket.getType() == DataPacket.TYPE_OP){
            Logger.logI("SERVER", "DP type Operation");
            if(listener != null){
                try{
                    Operation operation = (Operation) ByteUtils.fromBytes(dataPacket.getData());
                    listener.onOperationRecieved(operation);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override //Network Manager
    public void broadcast(Object obj){
        Logger.logI("SERVER", "broadcasting Object: " + obj.toString());
        for(DataPacketReceiver receiver: receivers){
            Socket socket = receiver.getSocket();
            try{
                /**
                 * TODO: Let's not create a new outputstream anytime we try to send a datapacket
                 * Store an output stream for each socket, that we don't close
                 */
                OutputStream streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                DataPacket dp = new DataPacket("Server", "Client", DataPacket.TYPE_OP, ByteUtils.toBytes(obj));
                streamOut.write(ByteUtils.toBytesWithLength(dp));
                streamOut.flush();
            }
            catch(Exception e){
                Logger.logE("SERVER", "Handle local diff failed on socket: " + socket.getInetAddress().toString());
                e.printStackTrace();
            }
        }

    }


    @Override
    public void attachListener(NetworkManagerListener listener){
        this.listener = listener;
    }

}
