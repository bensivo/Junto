package main.networking;

import main.DataPacket;
import main.util.ByteUtils;
import main.util.IPUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

/**
 * Connects to a server running on the same network.
 * Once the connection is established, DataPackets can be sent
 * to the Server
 */
public class JuntoClient {
    private Socket socket = null;
    private BufferedReader  console = null;
    private DataOutputStream streamOut = null;
    private DataInputStream inStream = null;

    private String serverName;
    private int serverPort;
    private boolean connected = false;

    public boolean isConnected(){return this.connected;}

    public JuntoClient(){
        this("", -1);
    }
    /**
     * Creates a socket to the given server on the given port, and starts listening to System.in
     * For user input to send to the server.
     * User input is continually sent to the server until ".bye" is sent
     * @param serverName the ip or hostname of the server machine
     * @param serverPort the port to use
     */
    public JuntoClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void start(){
        System.out.println("Trying to connect to " + serverName + " on port " + serverPort);
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connection established");
            openStreams();
            connected = true;
        }
        catch(UnknownHostException e) {
            connected = false;
            System.out.println("Couldn't find server on specified ip: " + e.getMessage());
        }
        catch(IOException ioe) {
            connected = false;
            System.out.println("IO Error:" + ioe.getMessage());
        }
    }

    public void stop(){
        closeStreams();
    }

    /**
     * Convert a DataPacket Object to an array of bytes (with a length packet appended
     * at the front), and send it across the socket
     * @param dataPacket The DataPacket to send
     */
    public void sendPacket(DataPacket dataPacket){
        try {
            byte[] bytes = ByteUtils.toBytesWithLength(dataPacket);
            streamOut.write(bytes);
            streamOut.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public DataPacket recievePacket(){
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
            byte[] dataPacketBytes = new byte[(int) length];

            /**
             * Read the DataPacket itself into the array
             * Convert it into a DataPacket Object with ByteUtils
             */
            inStream.read(dataPacketBytes, 0, (int) length);
            DataPacket dataPacket = (DataPacket) ByteUtils.fromBytes(dataPacketBytes);
            return  dataPacket;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void openStreams() throws IOException {
        console   = new BufferedReader(new InputStreamReader(System.in));
        streamOut = new DataOutputStream(socket.getOutputStream());
        inStream = new DataInputStream(socket.getInputStream());
    }

    public void closeStreams() {
        try {  
            if (console   != null)  console.close();
            if (streamOut != null)  streamOut.close();
            if (socket    != null)  socket.close();
        }
        catch(IOException ioe) {  
            System.out.println("Error closing ...");
        }
    }

    /**
     * Start the client application
     * @param args <Server name (ip or hostname)> <port number>
     */
    public static void main(String args[]) {  
        JuntoClient client = null;

        if (args.length != 2) {
            System.out.println("Usage: java JuntoClient host port");
        }

        List<String> ipAddresses = IPUtils.getMyIPAddresses();
        System.out.println("Select your ip address");
        for(int i=0; i<ipAddresses.size(); i++){
            System.out.println("    " + i + ".) " + ipAddresses.get(i));
        }
        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();
        String myIP = ipAddresses.get(selection);

        ipAddresses = IPUtils.scanLocalNetwork(myIP);
        for(int i=0; i<ipAddresses.size(); i++){
            System.out.println("    " + i + ".) " + ipAddresses.get(i));
        }
        selection = scanner.nextInt();
        String hostAddress = ipAddresses.get(selection);

        client = new JuntoClient(hostAddress, 5001);
        client.start();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
