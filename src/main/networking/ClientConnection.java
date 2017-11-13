package main.networking;

import main.DataPacket;
import main.util.FileIO;
import main.util.ByteUtils;
import main.util.FilePathUtils;

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
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;

    public ClientConnection(Socket socket){
        this.socket = socket;
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
                        case DataPacket.TYPE_FILE:
                            System.out.println("Data Packet is a file");
                            //Convert given filepath to Cloud10 filepath
                            String filepath = dataPacket.getFilepath();
                            System.out.println("Original filename: " + filepath);
                            Path p = Paths.get(filepath);
                            String localFilePath = FilePathUtils.getCloud10Dir() + FilePathUtils.getFilePathSeparator() + p.getFileName().toString();
                            System.out.println("Local filepath: " + filepath);

                            FilePathUtils.createCloud10DirIfNotCreated();
                            FileIO.writeFile(localFilePath, dataPacket.getData());
                            System.out.println("File written");

                            break;

                        case DataPacket.TYPE_DIR_REQUEST:
                            System.out.println("Data Packet is a Directory Request");
                            List<String> filepaths = new ArrayList();
                            /**
                             *     ,--._
                                    `.   `.                      ,-.
                                      `.`. `.                  ,'   )
                                        \`:  \               ,'    /
                                         \`:  ),.         ,-' ,   /
                                         ( :  |:::.    ,-' ,'   ,'
                                         `.;: |::::  ,' ,:'  ,-'
                                         ,^-. `,--.-/ ,'  _,'
                                        (__        ^ ( _,'
                                      __((o\   __   ,-'
                                    ,',-.     ((o)  /
                                  ,','   `.    `-- (
                                  |'      ,`        \
                                  |     ,:' `        |
                                 (  `--      :-.     |
                                 `,.__       ,-,'   ;
                                 (_/  `,__,-' /   ,'
                                 |\`--|_/,' ,' _,'
                                 \_^--^,',-' -'(
                                 (_`--','       `-.
                                  ;;;;'       ::::.`------.
                                    ,::       `::::  `:.   `.
                                   ,:::`       :::::   `::.  \
                                  ;:::::       `::::     `::  \
                                  |:::::        `::'      `:   ;
                                  |:::::.        ;'        ;   |
                                  |:::::;                   )  |
                                  |::::::        ,   `::'   |  \
                                  |::::::.       ;.   :'    ;   ::.
                                  |::::,::        :.  :   ,;:   |::
                                  ;:::;`"::     ,:::  |,-' `:   |::,
                                  /;::|    `--;""';'  |     :. (`";'
                                  \   ;           ;   |     ::  `,
                                   ;  |           |  ,:;     |  :
                                   |  ;           |  |:;     |  |
                                   |  |           |  |:      |  |
                                   |  |           |  ;:      |  |
                                  /___|          /____|     ,:__|
                                 /    /  -hrr-   /    |     /    )
                                 `---'          '----'      `---'

                             ADD CODE TO GET LIST OF FILEPATHS HERE
                             */
                            filepaths.add("Filepath1");
                            filepaths.add("Filepath2");
                            filepaths.add("Filepath3");
                            filepaths.add("Filepath4");
                            filepaths.add("Filepath5");
                            filepaths.add("Filepath6");

                            byte[] dataBytes = ByteUtils.toBytes(filepaths);
                            DataPacket response = new DataPacket("Server", "Client", DataPacket.TYPE_DIR_REQUEST, dataBytes);
                            byte[] resPacketBytes = ByteUtils.toBytesWithLength(response);
                            outStream.write(resPacketBytes);
                            outStream.flush();

                            break;
                    }
                    if(dataPacket.getType() == DataPacket.TYPE_FILE){
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
