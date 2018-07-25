package main.networking.core;
/**
 * Abstraction for sending data through a socket connection.
 * Usage as follows:
 * - Get Data you wish to send over internet connection (some Object)
 * - Create a DataPacket object
 * - Call the toBytes() function
 * - Send the bytes over the socket connection
 * - On the other end, call the fromBytes() function to create an identical dataPacket object
 * - Retrieve the data from the packet
 *
 * Note: Once created, DataPackets are immutable
 */

import java.io.Serializable;
import java.util.Map;

public class DataPacket implements Serializable {
    public final static int TYPE_DIFF = 1;
    public final static int TYPE_OP = 2;
    public final static int TYPE_AUTH = 3;
    public final static int TYPE_DIR_REQUEST = 4;

    public final Map<String, Object> meta;
    public final byte data[];
    public final int type;


    public DataPacket(byte[] data, int type, Map<String, Object> meta){
        this.type = type;
        this.data = data;
        this.meta = meta;
    }

    public DataPacket() {
        this(null, 0, null);
    }
}
