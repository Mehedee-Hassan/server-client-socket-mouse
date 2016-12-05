package mousepad.server;

import java.net.InetAddress;

/**
 * Created by mhr on 06-Dec-16.
 */
public class Utilities {

    public String MY_HOST_NAME;


    public Utilities(){
        MY_HOST_NAME = getHostName();
    }


    private static String getHostName() {
        String name = "LocalHost";
        try {
            InetAddress address = InetAddress.getLocalHost();

            name = address.getHostName();
        }
        catch (Exception ex){}
        return name;
    }
}
