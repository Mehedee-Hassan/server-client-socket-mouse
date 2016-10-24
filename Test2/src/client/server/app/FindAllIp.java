package client.server.app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by mhr on 10/22/16.
 */
public class FindAllIp {



    public static void main(String [] args){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = addr.getHostAddress();
        System.out.println("Ip: " + ip);
    }

    public static void main4(String[] args) throws UnknownHostException {
        try {

            String dstAddress ="192.168.1.100";

            byte[] ip = new byte[4];//= localhost.getAddress();

            String[] temp = dstAddress.split(Pattern.quote("."));
            int t = 0;
            ip[0] = (byte)(Integer.parseInt(temp[0]));
            ip[1] = (byte)(Integer.parseInt(temp[1]));
            ip[2] = (byte)(Integer.parseInt(temp[2]));
            ip[3] = (byte)(Integer.parseInt(temp[3]));

            ip[0] &=  0xFF;
            ip[1] &=  0xFF;
            ip[2] &=  0xFF;
            ip[3] &=  0xFF;

            InetAddress address = InetAddress.getByName("192.168.1.100");



            System.out.println("My name is " + address.getHostName());
        } catch (UnknownHostException e) {
            System.out.println("I'm sorry. I don't know my own name.");
        }
    }


    public static void main3(String[] args) throws IOException {
        // Fetch local host
        InetAddress localhost = InetAddress.getLocalHost();

        // IPv4 usage
        byte[] ip = localhost.getAddress();

        // looping
        for (int i = 1; i <= 254; i++)
        {
            ip[3] = (byte)i;
            InetAddress address = InetAddress.getByAddress(ip);
            if (address.isReachable(1000))
            {
                System.out.println(address + " - Pinging... Pinging");
            }
            else if (!address.getHostAddress().equals(address.getHostName()))
            {
                System.out.println(address + " - DNS lookup known..");
            }
            else
            {
                //System.out.println(address + " - the host address and the host name are same");
            }
        }

    }




    public static void main2(String [] args){
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while(nis.hasMoreElements())
            {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration ias = ni.getInetAddresses();
                while (ias.hasMoreElements())
                {
                    InetAddress ia = (InetAddress) ias.nextElement();
                    System.out.println(ia.getHostAddress());
                }

            }
        } catch (SocketException ex) {
            Logger.getLogger(FindAllIp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
