package thread.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Created by mhr on 10/24/16.
 */
public class ThreadTest {


    public static void main(String [] args){




        byte[] nextIp = nextIp(getHostAddress());

        for(int i =1 ; i <=255 ; i ++) {

            nextIp[3] = (byte) i;
            (new Thread(new TTest(nextIp))).start();
        }
    }

    private static byte[] nextIp(String dstAddress) {




        String[] temp = dstAddress.split(Pattern.quote("."));


        // IPv4 usage
        byte[] ip = new byte[4];//= localhost.getAddress();


        int t = 0;
        ip[0] = (byte)(Integer.parseInt(temp[0]));
        ip[1] = (byte)(Integer.parseInt(temp[1]));
        ip[2] = (byte)(Integer.parseInt(temp[2]));
        ip[3] = (byte)(Integer.parseInt(temp[3]));

        ip[0] &=  0xFF;
        ip[1] &=  0xFF;
        ip[2] &=  0xFF;
        ip[3] &=  0xFF;





        return ip;
    }



    private static String getHostAddress() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = addr.getHostAddress();


        return ip;
    }



}


class TTest implements Runnable{

    String TAG = "TTest";
    byte[] ipAddress ;

    public TTest(){

    }

    public TTest(byte[] ipAddress){
        this.ipAddress = ipAddress;
    }

    @Override
    public void run() {
        System.out.println ("new thread");

             test(getHostAddress());

     }

    private String getHostAddress() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = addr.getHostAddress();


        return ip;
    }


    void test(String dstAddress){
         Socket socket = null;



         //===
         try {


             {
                 InetAddress address = InetAddress.getByAddress(ipAddress);

                 //Log.d(TAG, "doInBackground: address =" +address);
                 if (address.isReachable(500)) {



                     try {

                         System.out.println(TAG+"doInBackground: address =" +address+" "+address.getHostName() +" "+address.getHostAddress()
                                 +" "+address.getCanonicalHostName()
                         );

                         socket = new Socket(address.getHostAddress(), 9000);

                         System.out.println(TAG+" try catch ="+address.toString());

                         DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                         DOS.writeUTF(address.getHostAddress());





                     } catch(Exception ex){
                         System.out.println(TAG+"exception ");


                     }


                     finally {
                         if (socket != null) {
                             try {
                                 socket.close();
                             } catch (IOException e) {


                                 e.printStackTrace();
                             }
                         }
                     }

                 } else if (!address.getHostAddress().equals(address.getHostName())) {


                 } else {
                     //System.out.println(address + " - the host address and the host name are same");
                 }
             }
         }catch (Exception ex){

         }
     }


}