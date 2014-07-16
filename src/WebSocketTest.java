/**
 * Created by Blake on 2014/4/8.
 */
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Timer;

public class WebSocketTest
{
//    public static String info=new String();
    public static ArrayList<String> info=new ArrayList<String>();
    public static void WriteAllInfoString(String input)//Write ArrayLists which will be sent to Client
    {
        for(int i=0;i<info.size();i++)
        {
            info.set(i,info.get(i)+input);
        }
    }
    public static void ClearTheInfoString(int index)//Clear ArrayLists which will be sent to Client
    {
        info.set(index,"");
    }
    public static void DeleteTheInfoString()//Delete ArrayLists which will be sent to Client
    {
        info.clear();
    }
    public static void main(String[] args) throws Exception
    {
        ADPathController.readADPath();
        //-----------------IMC Client Controller Initialize-----------------------------------------------------
        //IMC_Client_Controller_Initialize
        IMC_Client_Controller.IMC_Client_Controller_Initialize();
        //Schedule
        IMC_Client_Controller.schedule(IMC_Client_Controller.timer, 0, IMC_Client_Controller.scheduleList);
//        IMC_Client_Controller.insertSchedule("0|2014/06/26/01/39|./Request/140624.xml", IMC_Client_Controller.scheduleList);
//        IMC_Client_Controller.timer=IMC_Client_Controller.reSchedule(IMC_Client_Controller.timer, 0, IMC_Client_Controller.scheduleList);
        Timer askAD=new Timer();
        askAD.scheduleAtFixedRate(new IMC_Client(IMC_Client_Controller.scheduleList,0,1),0,30000);
        Timer askAD_M=new Timer();
        askAD_M.scheduleAtFixedRate(new IMC_Client(IMC_Client_Controller.scheduleList,0,2),0,30000);
        //-----------------Web Socket-----------------------------------------------------
        info=new ArrayList<String>();
        Server server = new Server(8080);
        WebSocketHandler wsHandler = new WebSocketHandler()
        {
            @Override
            public void configure(WebSocketServletFactory factory)
            {
                factory.register(MyWebSocketHandler.class);
            }
        };
        server.setHandler(wsHandler);
        server.start();
//        server.join();
        //-----------------Monitor Socket Handler------------------------------------------------------
        ServerSocket Monitorserver = new ServerSocket(8082);
        System.out.println("waiting Resource Monitor connect..");
        while (true)
        {
            // transfer location change Single User or Multi User
            MonitorServer mc = new MonitorServer(Monitorserver.accept());
            mc.start();
        }
    }
}