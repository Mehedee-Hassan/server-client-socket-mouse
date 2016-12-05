package mousepad.server;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by mhr on 06-Dec-16.
 */
public class Commands {


    private Commands(){

    }

    private String hostName ;

    public Commands(String hostName){

        this.hostName = hostName;

    }

    public void searchedForServer(Socket clientSocket) throws IOException {
        DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
        outToClient.writeUTF(hostName);
        outToClient.writeUTF(hostName);
        outToClient.writeUTF(hostName);
    }
    public void closeWindow(Robot robot) {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_F4);
    }

    public static void mouseLeftClick(Robot robot) {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
