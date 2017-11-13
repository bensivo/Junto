package main;

import main.networking.JuntoServer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * The entry point of the Server's command line interface
 * The GUI server application can be found at cloud10.main.server.gui.MainWindow
 *
 * @author Benjamin
 */
public class ServerMain{

    public static void main(String args[]){
        JuntoServer server = null;
        int port;
        
        if (args.length != 1) {
            System.out.println("Usage: java JuntoServer port");
            System.exit(1);
        }

        //Print all my ip addresses
        try{
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()){

                NetworkInterface ni = (NetworkInterface) e.nextElement();
                System.out.println(ni.getDisplayName());
                Enumeration ee = ni.getInetAddresses();
                while(ee.hasMoreElements()){
                    InetAddress inetAddress  = (InetAddress)ee.nextElement();
                    System.out.println("    "+inetAddress.getHostAddress());
                }
            }
        }catch(SocketException se){

        }

        //Start the server
        port = Integer.parseInt(args[0]);
        server = new JuntoServer(port);
    }
}
