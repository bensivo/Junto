package main.networking.interfaces;

import main.DataPacket;
import main.networking.DataPacketReceiver;

public interface DataPacketReceiverListener {
    void onDataPacketReceived(DataPacket dp, DataPacketReceiver dpr);
}
