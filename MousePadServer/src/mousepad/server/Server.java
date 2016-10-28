package mousepad.server;

import mousepad.server.constant.Constant;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
    private static String COMMAND_CLOSE_WINDOW = "7";
    private static String COMMAND_TAB_WINDOW = "8";
    private static String COMMAND_ENTER = "9";

    public static void main(String [] args) throws IOException{


        ServerSocket serverSocket = new ServerSocket(9000);



        while (true) {

            Socket clientSocket = null;
            clientSocket = serverSocket.accept();


            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());


            if(serverSocket.isClosed() || serverSocket == null)
                serverSocket = new ServerSocket(9000); // some times after a long time data pass exception occur

            String flag_or_message = dataInputStream.readUTF();


            System.out.println("flag_or_message_read="+flag_or_message);


            if (flag_or_message.equalsIgnoreCase(FLAG)) {
                //flag_block = 1;

                //if (flag_block == 1)
                System.out.println("search ="+flag_or_message);

                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                outToClient.writeUTF(getHostName());


            }else if(flag_or_message.equalsIgnoreCase(COMMAND_CLOSE_WINDOW)) {

                try {
                    System.out.println("close window block try");
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ALT);
                    robot.keyPress(KeyEvent.VK_F4);
                    robot.keyRelease(KeyEvent.VK_ALT);
                    robot.keyRelease(KeyEvent.VK_F4);

                } catch (AWTException e) {

                    System.out.println("close window block exception");

                    e.printStackTrace();
                }
            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_TAB_WINDOW)) {

                try {
                    System.out.println("close window block try");
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ALT);
                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_ALT);
                    robot.keyRelease(KeyEvent.VK_TAB);

                } catch (AWTException e) {

                    System.out.println("close window block exception");

                    e.printStackTrace();
                }
            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_ENTER)) {

                try {
                    System.out.println("close window block try");
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ALT);
                    robot.keyPress(KeyEvent.VK_F4);
                    robot.keyRelease(KeyEvent.VK_ALT);
                    robot.keyRelease(KeyEvent.VK_F4);

                } catch (AWTException e) {

                    System.out.println("close window block exception");

                    e.printStackTrace();
                }
            }
            else
            //else if (flag_block == 2)
            {

                System.out.println("else block");

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

            if(flag_i == Constant.Action.SHORT_TOUCH){

                robot.mousePress( InputEvent.BUTTON1_MASK );
                robot.mouseRelease( InputEvent.BUTTON1_MASK );

            }else{
                robot.mouseMove((int)xf+currentX,(int)yf+currentY);

                currentX = MouseInfo.getPointerInfo().getLocation().x;
                currentY = MouseInfo.getPointerInfo().getLocation().y;

            }


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
