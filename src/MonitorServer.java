import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MonitorServer extends Thread
{
    private Socket client;

    public MonitorServer(Socket c)
    {
        this.client = c;
    }

    public void run()
    {
        try
        {
            System.out.println("Monitor Socket Handler Connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
            // Mutil User but can't parallel
            while (true)
            {
                String str=in.readLine();
                String[] commandArray=str.split("\\|");
                System.out.println(str);
                FileWriter fw = new FileWriter("./Log/info.log",true);
                if(commandArray[0].equals("A"))// who am I
                {
                    fw.write(str+"\n");
                    fw.flush();
                    WebSocketTest.WriteAllInfoString(str+"\n");
                }
                else if(commandArray[0].equals("C"))//Ryu status
                {
                    fw.write(str+"\n");
                    fw.flush();
                    WebSocketTest.WriteAllInfoString(str+"\n");
                }
                else if(commandArray[0].equals("D"))//OpenStack Loading
                {
                    fw.write(str+"\n");
                    fw.flush();
                    WebSocketTest.WriteAllInfoString(str+"\n");
                }
                else if(commandArray[0].equals("E"))//Network Loading
                {
                    fw.write(str+"\n");
                    fw.flush();
                    WebSocketTest.WriteAllInfoString(str+"\n");
                }
                else if(commandArray[0].equals("F"))//Controller換手
                {
                    fw.write(str+"\n");
                    fw.flush();
                    WebSocketTest.WriteAllInfoString(str+"\n");
                }
                fw.close();
                out.println("has receive....");
                out.flush();
//                getClientCommand(out,str);
                if (str.equals("end"))
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
}