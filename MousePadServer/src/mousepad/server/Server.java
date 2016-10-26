package mousepad.server;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by mhr on 10/21/16.
 */
public class Server {
    public static String FLAG = "_SEARCH";
    public static String N_FLAG = "_MOUSE";
    private static boolean firstTimeCoordinate = true;
    private static int lastx = 0;
    private static int lasty = 0;

    public static int flag_block = -1;
    public static void main(String [] args) throws IOException{


        ServerSocket serverSocket = new ServerSocket(9000);


        while (true) {

            Socket clientSocket = null;
            clientSocket = serverSocket.accept();


            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());


            if(serverSocket.isClosed() || serverSocket == null)
                serverSocket = new ServerSocket(9000); // some times after a long time data pass exception occur

            String flag_or_message = dataInputStream.readUTF();


            System.out.println(flag_or_message);


            if (flag_or_message.equalsIgnoreCase(FLAG)) {
                //flag_block = 1;

                //if (flag_block == 1)

                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                outToClient.writeUTF(getHostName());


            }else
            //else if (flag_block == 2)
            {
                // clientSocket = serverSocket.accept();
                // dataInputStream = new DataInputStream(clientSocket.getInputStream());
                // String message = dataInputStream.readUTF();

                String[] coordinate = flag_or_message.split(" ");

                System.out.println(coordinate[0]+" "+coordinate[1]+" "+coordinate[2]);

                setMousePosition(coordinate[0],coordinate[1],coordinate[2]);


            }


        }
    }
    public static  int currentX = 0;
    public static  int currentY = 0;

    private static void setMousePosition(String flag ,String xpos ,String ypos) {


        System.out.println(" == "+xpos+" "+ypos);


        try {
            int flag_i = Integer.parseInt(flag);
            float xf = Float.parseFloat(xpos);
            float yf = Float.parseFloat(ypos);
            System.out.println((int)xf+" "+(int)yf);



            Robot robot = new Robot();


            System.out.println("=current mouse pos ="+currentX+" "+currentY);
            robot.mouseMove((int)xf+currentX,(int)yf+currentY);



            currentX = MouseInfo.getPointerInfo().getLocation().x;
            currentY = MouseInfo.getPointerInfo().getLocation().y;


            if(flag_i == 2){
                System.out.println("UP");
                //firstTimeCoordinate = true;

                 //currentX = (int)MouseInfo.getPointerInfo().getLocation().getX();
                 //currentY = (int)MouseInfo.getPointerInfo().getLocation().getY();

                lastx = (int)xf;
                lasty = (int)yf;
            }




        } catch (AWTException e) {
            e.printStackTrace();
        }


    }

    private static void sendHostNameToClient(String ip) throws UnknownHostException {
        String name =  getHostName();

        Socket socket = null;
        try {
            socket = new Socket(ip, 9000);

            DataOutputStream DOS = null;
            DOS = new DataOutputStream(socket.getOutputStream());
            DOS.writeUTF("HELLO_WORLD");

            System.out.print("sendhost name ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(socket!=null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }



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
