import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Blake on 2014/6/26.
 */
//For Simulate IMC Server
public class IMC_Server_Monitor extends Thread
{
        private Socket client;

        public IMC_Server_Monitor(Socket c)
        {
            this.client = c;
        }
        public void run()
        {
            try
            {
                System.out.println("Connected");
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream());
                // Mutil User but can't parallel
                while (true)
                {
                    String str=in.readLine();
                    String[] commandArray=str.split("\\|");
                    System.out.println("Received Msg: "+str);
                    if(commandArray[0].equals("M"))// who am I
                    {
                        if(commandArray[1].equals("Gene"))
                        {
                            System.out.println("Receive "+str);
                            out.flush();
                            out.println("O|Gene|/home/nwlab/ad/AD.xml|");
                            out.flush();
                            break;
                        }
                        else if(commandArray[1].equals("Moni"))
                        {
                            System.out.println("Receive "+str);
                            out.flush();
                            out.println("O|Moni|/home/nwlab/ad/AD_M.xml||");
                            out.flush();
                            break;
                        }
                    }
                    else if(commandArray[0].equals("L"))
                    {
                        System.out.println("Receive "+str);
                        break;
//                        out.flush();
//                        out.println("Received L");
//                        out.flush();
                    }
                    if (commandArray[0].equals("Z"))
                        break;
                }
                client.close();
            }
            catch (IOException ex)
            {

            }
            finally
            {

            }
        }
        public static void main(String[] args) throws Exception
        {
            ServerSocket IMCserver = new ServerSocket(8081);
            System.out.println("waiting connect..");
            while (true)
            {
                // transfer location change Single User or Multi User
                IMC_Server_Monitor mc = new IMC_Server_Monitor(IMCserver.accept());
                mc.start();
            }


        }
}
