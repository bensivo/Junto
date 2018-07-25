package main.networking;

import main.networking.core.DataPacket;
import main.networking.core.DataPacketReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes incoming dataPackets, and routes them to different locations based
 * on registered listeners. Routing rules are defined by the listeners themselves.
 */
public class DataPacketRouter {
    public interface Receiver{
        boolean shouldReceivePacket(DataPacket dp);
        void receivePacket(DataPacket dp);
    }

    List<Receiver> packetReceivers;

    public DataPacketRouter(){
        this.packetReceivers = new ArrayList<>();
    }

    public void registerReceiver(Receiver receiver){
        this.packetReceivers.add(receiver);
    }

    public void routeDataPacket(DataPacket dp){
        for(Receiver receiver: this.packetReceivers){
            if(receiver.shouldReceivePacket(dp)){
                receiver.receivePacket(dp);
            }
        }
    }
}
