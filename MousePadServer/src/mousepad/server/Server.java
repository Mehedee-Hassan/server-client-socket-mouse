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
    private static final String COMMAND_CLOSE_WINDOW = "7";
    private static final String COMMAND_TAB_WINDOW = "8";
    private static final String COMMAND_ENTER = "9";
    private static final String COMMAND_SCROLL_VERTICAL_DOWN = "10";
    private static final String COMMAND_SCROLL_VERTICAL_UP = "11";
    private static final String COMMAND_ESC = "12";
    private static final String COMMAND_MOUSE_LEFT_CLICK = "13";
    private static final String COMMAND_MOUSE_RIGHT_CLICK = "14";
    private static boolean mouseLongClick = false;


    public static void main(String [] args) throws IOException, AWTException {

        Robot robot = new Robot();

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


                    System.out.println("close window block try");
                   // Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ALT);
                    robot.keyPress(KeyEvent.VK_F4);
                    robot.keyRelease(KeyEvent.VK_ALT);
                    robot.keyRelease(KeyEvent.VK_F4);


            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_MOUSE_LEFT_CLICK)) {


                // Robot robot = new Robot();
                robot.mousePress( InputEvent.BUTTON1_MASK );
                robot.mouseRelease( InputEvent.BUTTON1_MASK );

            }
            else if(flag_or_message.equalsIgnoreCase("15")) {


                // Robot robot = new Robot();
                robot.mousePress( InputEvent.BUTTON1_MASK );
                mouseLongClick = true;
            }
            else if( flag_or_message.equalsIgnoreCase("16")) {


            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_MOUSE_RIGHT_CLICK)) {



                   // Robot robot = new Robot();
                    robot.mousePress( InputEvent.BUTTON3_MASK );
                    robot.mouseRelease( InputEvent.BUTTON3_MASK );


            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_ESC)) {


                    System.out.println("close window block try");
                   // Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    robot.keyRelease(KeyEvent.VK_ESCAPE);


            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_ENTER)) {


                    System.out.println("close window block try");
                  //  Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);

            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_SCROLL_VERTICAL_DOWN)) {




                    System.out.println("scroll down block");
                   // Robot robot = new Robot();
                    robot.mouseWheel(1);



            }
            else if(flag_or_message.equalsIgnoreCase(COMMAND_SCROLL_VERTICAL_UP)) {


                    System.out.println("close window block try");
                 //   Robot robot = new Robot();
                    robot.mouseWheel(-1);


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


                if(mouseLongClick){

                    robot.mouseRelease( InputEvent.BUTTON1_MASK );
                    mouseLongClick = false;
                }else{
                    robot.mousePress( InputEvent.BUTTON1_MASK );
                    robot.mouseRelease( InputEvent.BUTTON1_MASK );
                }

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
