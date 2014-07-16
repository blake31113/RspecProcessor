/**
 * Created by Blake on 2014/4/8.
 */
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class MyWebSocketHandler
{//WebSocket Handler(Server)
    private boolean haveloadAD=false;
    private String Xml ="";
    private String requestXmlPath="./Request/";
    private String logPath="./Log/";
    private ArrayList<String> requestXmlBucket=new ArrayList<String>();
    private static ArrayList<Integer> hashcodeToIndex=new ArrayList<Integer>();
    private int numberOfWeb=0;
    public ArrayList<String> searchRequestXml()//Load Request List
    {
        StringBuffer fileList = new StringBuffer();
        try
        {
            java.io.File folder = new java.io.File(requestXmlPath);
            String[] list = folder.list();
            for(int i = 0; i < list.length; i++)
            {
                if(!requestXmlBucket.contains(list[i]))
                    requestXmlBucket.add(list[i]);
            }
        }
        catch(Exception e)
        {
            System.out.println("'"+requestXmlPath+"'isn't exist");
        }
        return requestXmlBucket;
    }
    public String loadXml(String path) throws IOException//Load AD or Request Xml
    {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        Xml ="";
        while (br.ready())
        {
            Xml = Xml +br.readLine()+"\n";
        }
        fr.close();
        return Xml;
    }
    private ArrayList<String> xmlBucket= new ArrayList<String>();
    @OnWebSocketClose
    public void onClose(int statusCode, String reason)//when connect close
    {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t)//when connect Error
    {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException//when Connect
    {

        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try
        {
            session.getRemote().sendString("Hello Webbrowser");
//            session.getRemote().sendString("%AD\n"+ loadXml("./Advertisement/Advertisement.xml"));
//            session.getRemote().sendString("%AD_M\n"+ loadXml("./Advertisement/Advertisement_M.xml"));
            System.out.println(ADPathController.Gene_Path);
            System.out.println(ADPathController.Moni_Path);
            session.getRemote().sendString("%AD\n"+ loadXml(ADPathController.Gene_Path));
            session.getRemote().sendString("%AD_M\n"+ loadXml(ADPathController.Moni_Path));
//            hashcodeToIndex.add(session.hashCode());
//            numberOfWeb++;
//            WebSocketTest.info.add("");
//            System.out.println("HashTable");
//            for(int i=0;i<hashcodeToIndex.size();i++)
//            {
//                System.out.println(hashcodeToIndex.indexOf(hashcodeToIndex.get(i))+"  "+hashcodeToIndex.get(i));
//            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session,String message) throws IOException, ParseException//when Receive Msg from client
    {
        if(message.indexOf("%")==0)
        {
            if(message.substring(message.indexOf("%")+1,message.indexOf("%%")).compareTo("SV")==0)//Save Request
            {
                String filename=message.substring(message.indexOf("%%")+2,message.indexOf("\n"));
                FileWriter fw = new FileWriter(requestXmlPath+filename);
                fw.write(message.substring(message.indexOf("\n")+1));
                fw.flush();
                fw.close();
                System.out.println("Successfully Save RequestXml from Web: "+filename);
            }
            else if(message.substring(message.indexOf("%")+1,message.indexOf("%%")).compareTo("LD")==0)//Load
            {
                if(message.substring(message.indexOf("%%")+2,message.indexOf("\n")).compareTo("REQLIST")==0)//Load Request List
                {
                    session.getRemote().sendString("%LD%%LIST%%%"+searchRequestXml());
                }
                else if(message.substring(message.indexOf("%%")+2,message.indexOf("%%%")).compareTo("XML")==0)//Load Request Xml
                {
                    String sendName=(message.substring(message.indexOf("%%%")+3,message.indexOf("\n")));
                    System.out.println("Web Load XML: "+sendName);
                    session.getRemote().sendString("%LD%%XML%%%\n"+ loadXml(requestXmlPath + sendName));
                }
            }
            else if(message.substring(message.indexOf("%")+1,message.indexOf("%%")).compareTo("SCD")==0)//Schedule
            {
                String scheduleInst=(message.substring(message.indexOf("%%")+2,message.indexOf("%%%")));
                System.out.println("Scheduling Inst: "+scheduleInst);
                IMC_Client_Controller.insertSchedule(scheduleInst, IMC_Client_Controller.scheduleList);
                IMC_Client_Controller.timer=IMC_Client_Controller.reSchedule(IMC_Client_Controller.timer, 0, IMC_Client_Controller.scheduleList);
            }
            else if(message.substring(message.indexOf("%")+1,message.indexOf("%%")).compareTo("MNT")==0)//Monitor Status
            {
//                String ran= String.valueOf((int)(Math.floor(Math.random()*5)));
//                session.getRemote().sendString("%MNT%%\n"+ loadXml(logPath+"info"+ran+".log"));
//                String path=logPath+"info.log";
//                File file = new File(path);
//                if(file.exists())
//                {
//                    session.getRemote().sendString("%MNT%%\n"+ loadXml(path));
//                    file.delete();
//                }
                if(hashcodeToIndex.contains(session.hashCode()))
                {
                  session.getRemote().sendString("%MNT%%\n"+WebSocketTest.info.get(hashcodeToIndex.indexOf(session.hashCode())));
                  WebSocketTest.ClearTheInfoString(hashcodeToIndex.indexOf(session.hashCode()));
                }
                else
                {
                    if(hashcodeToIndex.size()>20)
                    {
                        hashcodeToIndex.clear();
                        WebSocketTest.DeleteTheInfoString();
                    }
                    hashcodeToIndex.add(session.hashCode());
                    WebSocketTest.info.add("");
                    System.out.println("HashTable");
                    for(int i=0;i<hashcodeToIndex.size();i++)
                    {
                        System.out.println(hashcodeToIndex.indexOf(hashcodeToIndex.get(i))+"  "+hashcodeToIndex.get(i));
                    }
                    session.getRemote().sendString("%MNT%%\n"+WebSocketTest.info.get(hashcodeToIndex.indexOf(session.hashCode())));
                    WebSocketTest.ClearTheInfoString(hashcodeToIndex.indexOf(session.hashCode()));
                }
            }
//            else if(message.substring(message.indexOf("%")+1,message.indexOf("%%")).compareTo("MNT_CL")==0)
//            {
//                int temp=hashcodeToIndex.indexOf(session.hashCode());
//                hashcodeToIndex.remove(temp);
//                WebSocketTest.info.remove(temp);
//                System.out.println("Monitor Close");
//                for(int i=0;i<hashcodeToIndex.size();i++)
//                {
//                    System.out.println(hashcodeToIndex.indexOf(hashcodeToIndex.get(i))+"  "+hashcodeToIndex.get(i));
//                }
//            }
        }
        else
        {
            System.out.println("From Server Message: "+message);
        }
    }
}
class hashCodeToIndex
{
    public int index;
    public int hash;
    public hashCodeToIndex(int i,int hashcode)
    {
        index=i;
        hash=hashcode;
    }
}