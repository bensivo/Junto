package main.networking;

import main.DataPacket;
import main.util.FileIO;
import main.util.ByteUtils;
import main.util.FilePathUtils;
import main.util.StringChange;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a single connection to a client. Upon creation, starts a socket connection to the client specified
 * and begins listening for messages/packets.
 *
 * @author Benjamin
 */
public class ClientConnection implements Runnable{
    public interface ClientConnectionListener{
        void onStringChangePacket(StringChange stringChange);
    }

    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private ClientConnectionListener listener = null;

    public ClientConnection(Socket socket){
        this.socket = socket;
    }

    public void registerListener(ClientConnectionListener listener){
        this.listener = listener;
    }

    /**
     * Kill the connection to this client instantly (without waiting for blocking calls to finish).
     * Note that this will often throw a SocketException (which is totally safe)
     */
    public void instantStop(){
        try{
            socket.close();
            inStream.close();
            outStream.close();
            Thread.currentThread().interrupt();
        }
        catch(IOException ieo){
            ieo.printStackTrace();
        }
        Thread.currentThread().interrupt();
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

                    /**
                     * Write it to a file
                     */
                    switch(dataPacket.getType()){
                        case DataPacket.TYPE_CHAT:
                            try {
                                StringChange stringChange = (StringChange) ByteUtils.fromBytes(dataPacket.getData());
                                System.out.println(stringChange.toStringShort());
                                if(this.listener != null){
                                    listener.onStringChangePacket(stringChange);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            break;
                    }
                }
                catch(EOFException eof){ //Thrown when the Client closes its end of the socket
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

    private void open() throws IOException{
        inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        outStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    private void close() throws IOException {
        if(socket != null){
            socket.close();
        }
        if(inStream != null){
            inStream.close();
        }
    }
}
