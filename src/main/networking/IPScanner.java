/**
 * A Simple test of an IPScanning Algorithm.
 * First, displays all the IP Addresses in use by the system, and prompts the user to select one
 * Then it searches through all the IP's on the same local network, printing out reachable IP Addresses 
 */
package main.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class IPScanner {

    public static void main(String args[]) {
        int port;

        if (args.length != 1) {
            System.out.println("Usage: java JuntoServer port");
            System.exit(1);
        }
        port = Integer.parseInt(args[0]);



        //Print all my ip addresses
        ArrayList<String> ipAddresses = new ArrayList<>();
        int counter = 0;
        try {
            System.out.println("IP Adresses:");
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                //Looping through network interfaces
                NetworkInterface ni = (NetworkInterface) e.nextElement();
                Enumeration ee = ni.getInetAddresses();
                while (ee.hasMoreElements()) {
                    //Looping through IP's in each interface
                    InetAddress inetAddress = (InetAddress) ee.nextElement();
                    System.out.println("    " + counter + ".) " + inetAddress.getHostAddress());
                    System.out.println("    " + counter + ".) " + inetAddress.getHostName());
                    ipAddresses.add(inetAddress.getHostAddress());
                    counter++;
                }
            }
        }
        catch( SocketException se){
            //pass
        }

        System.out.println("What is my ip address? ");
        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();
        try{
            System.out.println("You chose: " + ipAddresses.get(selection));
            scanLANWithPort(ipAddresses.get(selection), 5001);
        }catch(IndexOutOfBoundsException e){
            System.out.println("Error: That was not a valid option");
            System.exit(1);
        }
    }

    public static void scanLANWithPort(String myIp, int port){
        String subnet = myIp.substring(0,myIp.lastIndexOf('.'));

        System.out.println("Searching for Hosts on subnet: " + subnet);
        for(int i=0; i<255; i++){
            try{
                String hostname = subnet + "." + i;
                InetAddress inetAddress = InetAddress.getByName(hostname);
                if(inetAddress.isReachable(10)){
                    System.out.println("Found Host: " + hostname);
                }

            }catch(UnknownHostException uhe){
            }
            catch(IOException ioe){
            }
        }
        System.out.println("Done searching.");



    }



}
