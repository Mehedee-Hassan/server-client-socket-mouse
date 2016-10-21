import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by mhr on 10/21/16.
 */
public class GatewayIp {
    public static void main(String [] args){

        String gateway= "";
        String adapter = "";

        //while(true)

        {
            try {

                Process result = Runtime.getRuntime().exec("netstat -rn");

                BufferedReader output = new BufferedReader
                        (new InputStreamReader(result.getInputStream()));

                String line = output.readLine();
                while (line != null) {
                    if (line.startsWith("default") == true)
                        break;
                    line = output.readLine();
                }


                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                gateway = st.nextToken();

                st.nextToken();
                st.nextToken();
                st.nextToken();

                adapter = st.nextToken();

                System.out.println(gateway + " " + adapter);

            } catch (Exception e) {
                System.out.println(e.toString());
                gateway = new String();
                adapter = new String();
                //break;
            }
        }
    }

}
