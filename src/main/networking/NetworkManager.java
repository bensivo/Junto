package main.networking;

import main.networking.core.DataPacket;
import main.networking.core.DataPacketReceiver;
import main.networking.interfaces.SocketAcceptor;
import main.networking.utils.ByteUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A Netowork Manager defines any class that has CONTROL over the main
 * networking operations of the app, such as network capabilities on and off.
 * Network managers must also define callbacks for recieved information.
 */
public abstract class NetworkManager implements SocketAcceptor, DataPacketReceiver.Listener{
    public static final int TYPE_CLIENT = 1;
    public static final int TYPE_SERVER = 2;

    protected DataPacketRouter dataPacketRouter;
    protected List<DataOutputStream> outputStreams;
    protected List<Thread> runningThreads;

    public NetworkManager(DataPacketRouter router){
        this.dataPacketRouter = router;
        this.runningThreads = new ArrayList<>();
        this.outputStreams = new ArrayList<>();
    }

    public abstract void start();

    public abstract void stop();

    /**
     * Handle the creation of a new socket, connecting to another Junto application.
     * In Clients, this socket is created in connect(). In servers, it's created by
     * the ConnectionPooler, as a result of a remote client's connection.
     *
     * @param socket The new socket
     */
    @Override
    public void onNewSocketConnected(Socket socket){
        try{
            // Create a receiver thread to start reading Data Packets from the new socket.
            DataPacketReceiver receiver = new DataPacketReceiver(socket, this);
            Thread t = new Thread(receiver);
            t.start();
            this.runningThreads.add(t);

            // Create and save a DataOutput stream for this socket.
            DataOutputStream streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.outputStreams.add(streamOut);

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Called from the DataPacketReceivers created in onNewSocketConnected
     *
     * @param dp The Data Packet
     * @param receiver The receiver that got the packet
     */
    @Override
    public void onDataPacketReceived(DataPacket dp, DataPacketReceiver receiver){
        this.dataPacketRouter.routeDataPacket(dp);
    }

    /**
     * Write a data object to all open DataOutputStreams (one for each socket that comes in)
     *
     * NOTE: The data object MUST be serializable.
     *
     * @param obj The object to be sent
     */
    public void broadcast(Object obj){
        for(DataOutputStream streamOut: this.outputStreams){
            try{
                DataPacket dp = new DataPacket(ByteUtils.toBytes(obj), DataPacket.TYPE_OP, null);
                streamOut.write(ByteUtils.toBytesWithLength(dp));
                streamOut.flush();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
