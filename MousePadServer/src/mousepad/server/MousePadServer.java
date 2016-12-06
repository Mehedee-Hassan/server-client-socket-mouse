package mousepad.server;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import mousepad.server.constant.Constant;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * Created by mhr on 10/21/16.
 */
public class MousePadServer {

    public static String N_FLAG = "_MOUSE";
    private static boolean firstTimeCoordinate = true;
    private static int lastx = 0;
    private static int lasty = 0;

    public static int flag_block = -1;

    private static boolean mouseLongClick = false;


    public static void main(String [] args) throws IOException, AWTException {


        systemTrayRun();


        final Robot robot = new Robot();
        Socket clientSocket = null;
        ServerSocket serverSocket = new ServerSocket(1239);

        Utilities utilities  = new Utilities();
        Commands runCommands = new Commands(utilities.MY_HOST_NAME);


        initExtraVars();


        while (true)
        {



            String flag_or_message;
            try {

                clientSocket = serverSocket.accept();


                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));


            if (serverSocket.isClosed() || serverSocket == null)
                serverSocket = new ServerSocket(1239);


                //if(dataInputStream.available() > 0 &&( flag_or_message = dataInputStream.readUTF())!=null)
                {




                    flag_or_message = dataInputStream.readUTF();


                    System.out.println("\nflag_or_message_read=" + flag_or_message);


                    if (flag_or_message.equalsIgnoreCase(Constant.Action.FLAG_SEARCH)) {

                        System.out.println("search =" + flag_or_message);
                        runCommands.searchedForServer(clientSocket);
                    }
                    else if(flag_or_message.equals(Constant.Action.FLAG_CONNECT_TO_PAD)){


                        Thread t = new Thread(new ServerThread(clientSocket,Constant.SOCKET_PORT,dataInputStream));
                        t.start();

                    }

                }
            }
            catch(Exception ex){
                    ex.printStackTrace();
                   System.out.print("Exception= "+ex.getMessage());
            }


        }


     }

    private static void initExtraVars() {

        initLastMousePosVars();
    }

    private static void initLastMousePosVars() {


        lastx = MouseInfo.getPointerInfo().getLocation().x;
        lasty = MouseInfo.getPointerInfo().getLocation().y;

    }


    private static void systemTrayRun() {
        System.out.println("system tray");


        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("images/mouse_circle10.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem exitItem = new MenuItem("Exit");
        MenuItem aboutItem = new MenuItem("About");

        exitItem.setActionCommand("_EXIT");
        aboutItem.setActionCommand("_ABOUT");

        exitItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equalsIgnoreCase("_EXIT")){
                    System.exit(0);
                }
            }
        });
        aboutItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if(e.getActionCommand().equalsIgnoreCase("_ABOUT")){
                    JOptionPane.showMessageDialog(null, "MousePad is an open source software" +
                            "\n\nApologizing for bad performance :( " +
                            "\n\nContact developer,\nmehedeehassan@outlook.com");

                }
            }
        });

        popup.add(aboutItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip("MousePad");

        trayIcon.setActionCommand("_MENU");

        trayIcon.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equalsIgnoreCase("_MENU")){

                    JOptionPane.showMessageDialog(null, "MousePad is Running");


                }
            }
        });

        trayIcon.setImageAutoSize(true);

        trayIcon.displayMessage("MousePad","MousePad is here", TrayIcon.MessageType.INFO);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }

    }
    protected static Image createImage(String path, String description) {
        URL imageURL = MousePadServer.class.getResource(path);

        System.out.println("image icon");

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public static  int currentX = 0;
    public static  int currentY = 0;

    private static void setMousePosition(String flag ,String xpos ,String ypos,Robot robot) {


        System.out.println(" == "+xpos+" "+ypos);


        try {

            String flag_int = flag;

            int flag_i = Integer.parseInt(flag);
//            float xf = Float.parseFloat(xpos);
//            float yf = Float.parseFloat(ypos);


            int tfx = Integer.parseInt(xpos);
            int tfy = Integer.parseInt(ypos);

        //    System.out.println((int)xf+" "+(int)yf);




            System.out.println("=current mouse pos ="+tfx+" "+tfy);


            if(flag.equals(Constant.Action.COMMAND_FINGURE_UP)){
               // initLastMousePosVars();
            }
            else
            if(flag.equals(Constant.Action.SHORT_TOUCH))
            {

                if(mouseLongClick){

                    robot.mouseRelease( InputEvent.BUTTON1_MASK );
                    mouseLongClick = false;
                }else{
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                }

            }
            if(flag.equals(Constant.Action.MOUSE_MOVE)) {
                System.out.print("mouse move");

                robot.mouseMove(tfx + MouseInfo.getPointerInfo().getLocation().x, tfy + MouseInfo.getPointerInfo().getLocation().y);
            }
          //  lastx = MouseInfo.getPointerInfo().getLocation().x;
          //  lasty = MouseInfo.getPointerInfo().getLocation().y;



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void sendHostNameToClient(String ip) throws UnknownHostException {
        String name =  getHostName();

        Socket socket = null;
        try {
            socket = new Socket(ip, 1239);

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
