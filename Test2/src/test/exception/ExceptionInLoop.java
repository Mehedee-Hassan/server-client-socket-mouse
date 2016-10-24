package test.exception;

/**
 * Created by mhr on 10/23/16.
 */
public class ExceptionInLoop {

    public static void main(String [] args){
        for(int i = 0 ; i < 10 ; i++){

            try{

                if(i == 5 )
                    throw new Exception();

            }catch (Exception ex){
                System.out.println("Exception" + i);

            }
            System.out.println("=" + i);


        }
     }
}
