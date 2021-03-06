package main.networking;

import main.DataPacket;
import main.networking.interfaces.DataPacketReceiverListener;
import main.util.ByteUtils;

import java.io.*;
import java.net.Socket;

/**
 * Given a socket (already opened), begins listening for DataPackets coming through the socket, using
 * The ConnectionLIstenerInterface to notify observers
 *
 * @author Benjamin
 */
public class DataPacketReceiver implements Runnable{

    private Socket socket;
    private DataInputStream inStream;
    DataPacketReceiverListener listener = null;

    public DataPacketReceiver(Socket socket) {
        this.socket = socket;
        this.listener = null;
    }

    public DataPacketReceiver(Socket socket, DataPacketReceiverListener receiverListener){
        this.socket = socket;
        this.listener = receiverListener;
    }


    public void attach(DataPacketReceiverListener listener){
        this.listener = listener;
    }


    /**
     * Called when the thread starts
     *
     * Using the given socket (which should already be connected to the client), listens for messages
     * and prints them to System.out
     *
     * Stops listening when ".bye" is read
     */
    public void run(){
        try {
            //Open a new socket for this client
            System.out.println("New Client connected: " + socket);
            open();

            String line = "";
            while (!line.equals(".bye") && !Thread.currentThread().isInterrupted()){
                try {
                    System.out.println("New DataPacket");
                    /**
                     * Read the initial Size Packet that has the length of the actual DataPacket
                     * Size Packets are always Long.Bytes (8) bytes long
                     */
                    byte[] size = new byte[Long.BYTES];
                    inStream.read(size, 0, Long.BYTES);

                    /**
                     * Initialize a byte array for the actual DataPacket
                     */
                    long length = ByteUtils.readSizePacket(size);
                    byte[] dataPacketBytes = new byte[(int)length];

                    /**
                     * Read the DataPacket itself into the array
                     * Convert it into a DataPacket Object with ByteUtils
                     */
                    inStream.read(dataPacketBytes,0,(int)length);
                    DataPacket dataPacket = (DataPacket)ByteUtils.fromBytes(dataPacketBytes);

                    listener.onDataPacketReceived(dataPacket, this);

                }
                catch(EOFException eof){ //Thrown when the Client closes its end of the socket
                    eof.printStackTrace();
                    System.out.println("Socket End of File");
                    this.instantStop();
                }
                catch(ClassNotFoundException e){ //Thrown when the incoming bytes can't be converted into a DataPacket object
                    e.printStackTrace();
                }
            }
            close();
            System.out.println("Client disconnected");
        }
        catch(IOException e){ //Errors in socket.open() or socket.close() will be caught here so they don't stop the whole program
            e.printStackTrace();
        }
        finally { //On some Exception, make sure all resources are freed
            try {
                close();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Kill the connection to this client instantly (without waiting for blocking calls to finish).
     * Note that this will often throw a SocketException on any thread reading from the socket
     */
    public void instantStop(){
        try{
            socket.close();
            inStream.close();
            Thread.currentThread().interrupt();
        }
        catch(IOException ieo){
            ieo.printStackTrace();
        }
        Thread.currentThread().interrupt();
    }

    private void open() throws IOException{
        inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    private void close() throws IOException {
        if(socket != null){
            socket.close();
        }
        if(inStream != null){
            inStream.close();
        }
    }

    public Socket getSocket(){
        return this.socket;
    }
}
