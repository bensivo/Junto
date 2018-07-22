package main.networking.core;
/**
 * Abstraction for sending data through a socket connection.
 * Usage as follows:
 * - Get Data you wish to send over internet connection
 * - Create a DataPacket object
 * - Call the toBytes() function
 * - Send the bytes over the socket connection
 * - On the other end, call the fromBytes() function to create an identical dataPacket object
 * - Retrieve the data from the packet
 *
 * The byte representation of the packet has a Header (256 bytes) and a body (variable length)
 */

import java.io.Serializable;

public class DataPacket implements Serializable {
    public final static int TYPE_DIFF = 1;
    public final static int TYPE_OP = 2;
    public final static int TYPE_AUTH = 3;
    public final static int TYPE_DIR_REQUEST = 4;

    private String filepath;
    private byte data[];
    private String source;
    private String destination;
    private int type;

    public DataPacket(String src, String dest, int type, byte[] data, String filepath){
        this.source = src;
        this.destination = dest;
        this.type = type;
        this.data = data;
        this.filepath = filepath;
    }

    public DataPacket(String src, String dest, int type, byte[] data){
        this(src,dest,type,data,"");
    }

    public DataPacket(int type, byte[] data){
        this("","",type, data, "");
    }

    public DataPacket() {
        this("","",0, new byte[0]);
    }

    public byte[] getData() { return data; }

    public void setData(byte[] data) { this.data = data; }

    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public String getFilepath() { return filepath; }

    public void setFilepath(String filepath) { this.filepath = filepath; }
}
