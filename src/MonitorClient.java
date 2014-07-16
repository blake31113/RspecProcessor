import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;


//for Simulating Monitor Client Send Component Status
public class MonitorClient
{
    static Socket server;
    static String MonitorCommand(String OPcode,String var1,String var2,String var3)
    {
        String command="";
        command=OPcode+"|"+var1+"|"+var2+"|"+var3;
        return  command;
    }
    public static ArrayList<String> loadXml(String path) throws IOException
    {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> Xml = new ArrayList<String>();
        while (br.ready())
        {
            Xml.add(br.readLine());
        }
        fr.close();
        return Xml;
    }
    public static void main(String[] args) throws Exception
    {
        server = new Socket("140.115.156.135",35004);
//        server = new Socket("127.0.0.1",8082);
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        PrintWriter out = new PrintWriter(server.getOutputStream());
        BufferedReader wt = new BufferedReader(new InputStreamReader(System.in));
//        out.println(MonitorCommand("A",InetAddress.getLocalHost().toString(),"Ryu_Monitor1",""));

        while (true)
        {
            String str=String.valueOf((int)Math.floor(Math.random()*5));// = wt.readLine();
            System.out.println(str);
//            out.println(str);
//            out.flush();
            if (str.equals("0"))
            {
                int ran= ((int)(Math.floor(Math.random()*10)));
                String temp=loadXml("./Log/info0.log").get(ran);
                System.out.println(temp);
                out.println(temp);
                out.flush();
                Thread.sleep(500);
            }
            else if (str.equals("1"))
            {
                int ran= ((int)(Math.floor(Math.random()*10)));
                String temp=loadXml("./Log/info1.log").get(ran);
                System.out.println(temp);
                out.println(temp);
                out.flush();
                Thread.sleep(500);
            }
            else if (str.equals("2"))
            {
                int ran= ((int)(Math.floor(Math.random()*10)));
                String temp=loadXml("./Log/info2.log").get(ran);
                System.out.println(temp);
                out.println(temp);
                out.flush();
                Thread.sleep(500);
            }
            else if (str.equals("3"))
            {
                int ran= ((int)(Math.floor(Math.random()*10)));
                String temp=loadXml("./Log/info3.log").get(ran);
                System.out.println(temp);
                out.println(temp);
                out.flush();
                Thread.sleep(500);
            }
            else if (str.equals("4"))
            {
                int ran= ((int)(Math.floor(Math.random()*10)));
                String temp=loadXml("./Log/info4.log").get(ran);
                System.out.println(temp);
                out.println(temp);
                out.flush();
                Thread.sleep(500);
            }
            if (str.equals("end"))
            {
                break;
            }
            System.out.println(in.readLine());
        }
        server.close();
    }
}