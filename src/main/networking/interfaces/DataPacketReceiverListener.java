package main.networking.interfaces;

import main.networking.core.DataPacket;
import main.networking.core.DataPacketReceiver;

/**
 * For classes that wish to be notified of new data packets
 */
public interface DataPacketReceiverListener {
    void onDataPacketReceived(DataPacket dp, DataPacketReceiver dpr);
}
