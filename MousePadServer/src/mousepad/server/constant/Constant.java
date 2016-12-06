package mousepad.server.constant;

public class Constant{


    public static final int SOCKET_PORT = 1239;

    public static class Action{

        public static final int DOWN                                = 1;
        public static final int UP                                  = 2;
        public static final String MOUSE_MOVE                       = "3";
        public static final int LONG_TOUCH                          = 6;
        public static final String SHORT_TOUCH                         = "5";
        public static final String COMMAND_CLOSE_WINDOW             = "7";
        public static final String COMMAND_TAB_WINDOW               = "8";
        public static final String COMMAND_ENTER                    = "9";
        public static final String COMMAND_SCROLL_VERTICAL_DOWN     = "10";
        public static final String COMMAND_SCROLL_VERTICAL_UP       = "11";
        public static final String COMMAND_ESC                      = "12";
        public static final String COMMAND_MOUSE_LEFT_CLICK         = "13";
        public static final String COMMAND_MOUSE_RIGHT_CLICK        = "14";
        public static final String COMMAND_CLICK_AND_HOLD           = "15";
        public static final String COMMAND_FINGURE_UP               = "16";
        public static final String FLAG_SEARCH                      = "18";
        public static final String FLAG_CONNECT_TO_PAD              = "4" ;
    }


}