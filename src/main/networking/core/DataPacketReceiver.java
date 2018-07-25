package main.networking.core;

import main.networking.utils.ByteUtils;
import main.util.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Used to listen for packets send over a socket.
 *
 * @author Benjamin
 */
public class DataPacketReceiver implements Runnable{

    public interface Listener{
        void onDataPacketReceived(DataPacket dp, DataPacketReceiver dpr);
    }

    private Socket socket;
    private DataInputStream inStream;
    DataPacketReceiver.Listener listener = null;

    public DataPacketReceiver(Socket socket) {
        this.socket = socket;
        this.listener = null;
    }

    public DataPacketReceiver(Socket socket, DataPacketReceiver.Listener receiverListener){
        this.socket = socket;
        this.listener = receiverListener;
    }


    public void attach(DataPacketReceiver.Listener listener){
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
            /**
             * TODO: Implement better thread termination
             */
            while (!line.equals(".bye") && !Thread.currentThread().isInterrupted()){
                try {
                    Logger.logI("DPR", "New Datapacket Received");
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
