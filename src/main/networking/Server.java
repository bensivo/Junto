package main.networking;

import main.networking.core.ConnectionPooler;
import main.networking.core.DataPacketReceiver;
import main.networking.interfaces.SocketAcceptor;
import main.networking.interfaces.NetworkManager;
import main.util.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Used to listen on a specific port for Junto Client applications to connect to.
 */
public class Server extends NetworkManager implements SocketAcceptor, DataPacketReceiver.Listener {
    private static final int PORT = 5001;

    private ConnectionPooler pooler; //Runnable that listens for new socket connections
    private Thread poolerThread;


    public Server(DataPacketRouter router){
        super(router);
        this.pooler = new ConnectionPooler(PORT);

        pooler.setSocketAcceptor(this);
        Logger.logI("SERVER", "New Server Created. Pooler created and registered");
    }


    @Override //NetworkManager
    public void start() {
        this.listen(PORT);
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

    @Override //NetworkManager
    public void stop() {
        poolerThread.interrupt();

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


    ///**
    // * What to do when a datapacket comes in from one of the DataPacketReceiver threads
    // * @param dataPacket
    // */
    //@Override //DataPacketReceiverListener
    //public void onDataPacketReceived(DataPacket dataPacket, DataPacketReceiver receiver) {
    //    Logger.logI("SERVER", "DataPacket received");

    //    /**
    //     * TODO: Push routing to another class (use the emitter and subscriber pattern)
    //     */
    //    /**
    //     * TODO: Implement operation forwarding (forwarding to all other clients)
    //     */
    //    if(dataPacket.type == DataPacket.TYPE_OP){
    //        Logger.logI("SERVER", "DP type Operation");
    //        if(listener != null){
    //            try{
    //                Operation operation = (Operation) ByteUtils.fromBytes(dataPacket.data);
    //                listener.onOperationRecieved(operation);
    //            }catch(Exception e){
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //}

    //@Override //Network Manager
    //public void broadcast(Object obj){
    //    Logger.logI("SERVER", "broadcasting Object: " + obj.toString());
    //    for(DataPacketReceiver receiver: receivers){
    //        Socket socket = receiver.getSocket();
    //        try{
    //            /**
    //             * TODO: Let's not create a new outputstream anytime we try to send a datapacket
    //             * Store an output stream for each socket, that we don't close
    //             */
    //            OutputStream streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    //            DataPacket dp = new DataPacket(ByteUtils.toBytes(obj), DataPacket.TYPE_OP, null);
    //            streamOut.write(ByteUtils.toBytesWithLength(dp));
    //            streamOut.flush();
    //        }
    //        catch(Exception e){
    //            Logger.logE("SERVER", "Handle local diff failed on socket: " + socket.getInetAddress().toString());
    //            e.printStackTrace();
    //        }
    //    }
    //}
}
