/**
 * Static Utility functions for managing the local network,
 * and manipulating IP addresses. 
 * 
 * @author Benjamin Sivoravong
 */

package main.networking.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtils{

    /**
     * Uses regex to determine if a given string is a valid IPv4 address
     * @param ip The ip address as a string
     * @return true if the ipaddress is a valid ipv4 address
     */
    public static boolean isValidIPv4Address(String ip){
        String ipPatternRegex = "^([0-9]|[1-8][0-9]|9[0-9]|1[0-9]{2}|2[0-4][0-9]|"+
                "25[0-5])\\.([0-9]|[1-8][0-9]|9[0-9]|1[0-9]{2}|2["+
                "0-4][0-9]|25[0-5])\\.([0-9]|[1-8][0-9]|9[0-9]|1["+
                "0-9]{2}|2[0-4][0-9]|25[0-5])\\.([0-9]|[1-8][0-9]"+
                "|9[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

        Pattern ipPattern = Pattern.compile(ipPatternRegex);
        Matcher ipMatcher = ipPattern.matcher(ip);

        return ipMatcher.find();
    }

    /**
     * Uses regex to determine if a given string can be used as a valid port
     * Won't accept reserved ports (ex. port 80)
     * @param ip The port
     * @return true if the port is valid
     */
    public static boolean isValidPort(String ip){
        String portPatternRegex = "^(102[4-9]|10[3-9][0-9]|1[1-9][0-9]{2}|[2-9][0"+
                "-9]{3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-"+
                "9]{2}|655[0-2][0-9]|6553[0-5])$";

        Pattern portPattern = Pattern.compile(portPatternRegex);
        Matcher portMatcher = portPattern.matcher(ip);

        return portMatcher.find();
    }

    /**
     * Find all the IP addresses registered on this machine
     * This includes IPv4, IPv6, and 127.0.0.1 (local host)
     * @return A list of IP Addresses registered on this machine
     */
    public static List<String> getMyIPAddresses(){
        ArrayList<String> ipAddresses = new ArrayList<>();

        //Print all my ip addresses
        try {
            //System.out.println("IP Adresses:");
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                //Looping through all my network interfaces
                NetworkInterface ni = (NetworkInterface) e.nextElement();
                Enumeration ee = ni.getInetAddresses();
                while (ee.hasMoreElements()) {
                    //Looping through the IPs in each interface
                    InetAddress inetAddress = (InetAddress) ee.nextElement();
                    ipAddresses.add(inetAddress.getHostAddress());
                }
            }
        }
        catch( SocketException se){
            //pass
        }
        return ipAddresses;
    }

    /**
     * Get a list of all the registered IPv4 addresses on your machine
     * Note: This will always include 127.0.0.1
     * @return A List object of IPv4 addresses registered on this machine
     */
    public static List<String> getMyIPv4Addresses(){
        ArrayList<String> ipV4Addresses = new ArrayList<>();
        for(String address: getMyIPAddresses()){
            if(isValidIPv4Address(address)){
                ipV4Addresses.add(address);
            }
        }
        return ipV4Addresses;
    }

    /**
     * Find all the IP Address of machines available on the Local Network given by a specific IP
     * NOTE: Don't call this function on 127.0.0.1
     *
     * @param myIp The IP address of this machine, specifying with LAN to scan
     * @return A list of reachable IP Address on teh same network as the given IP
     */
    public static List<String> scanLocalNetwork(String myIp){
        ArrayList<String> ipAddresses = new ArrayList<>();
        String subnet = myIp.substring(0,myIp.lastIndexOf('.'));

        for(int i=0; i<255; i++){
            try{
                String hostname = subnet + "." + i;
                InetAddress inetAddress = InetAddress.getByName(hostname);
                if(inetAddress.isReachable(10)){
                    ipAddresses.add(inetAddress.getHostAddress());
                }
            }catch(UnknownHostException uhe){} //thrown by getByName()
            catch(IOException ioe){} //Thrown by isReachable()
        }

        return ipAddresses;
    }
}
