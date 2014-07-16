import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by Blake on 2014/6/10.
 */
public class IMC_Client extends TimerTask
{
    private String procedurePath="./Procedure/";
    private Socket IMCserver;
    private String filename;
    private String requestXml;
    private int listIndex;
    private int clientType;
    private ArrayList<ScheduleRecord> scheduleList;
    public IMC_Client(ArrayList<ScheduleRecord> myList,int index,int inputType)
    {
        scheduleList=myList;
        if(myList.size()>0)
            filename=myList.get(index).getfilename();
        listIndex=index;
        clientType=inputType;
    }
    public void run()
    {
        System.out.println("Task 執行時間：" + new Date());
        try
        {
            IMCserver = new Socket("127.0.0.1",8081);
        }
        catch (IOException e)
        {
            System.out.println("Error: Can't Connect to Server");
//            e.printStackTrace();
        }
        try
        {
            if(IMCserver!=null&&IMCserver.isConnected())//when socket Connenct
            {
                BufferedReader in = null;
                PrintWriter out=null;
                in 	= new BufferedReader(new InputStreamReader(IMCserver.getInputStream()));
                out = new PrintWriter(IMCserver.getOutputStream());
//                System.out.println("Type: "+clientType);
                if(clientType==0)//Schedule
                {
                    File f= new File(filename);
                    String sendMsg="L|"+f.getAbsolutePath()+"||";
                    out.println(sendMsg);
                    out.flush();
                    System.out.println("Send to IMC Server Msg: "+sendMsg);
                    System.out.println("Send to IMC Server Request File: "+f.getAbsolutePath());
                    IMC_Client_Controller.deleteScheduleByIndex(listIndex,scheduleList);
                    IMC_Client_Controller.sortSchedule(scheduleList);
                    IMC_Client_Controller.rewriteSchedule(scheduleList);
                    IMC_Client_Controller.timer=IMC_Client_Controller.reSchedule(IMC_Client_Controller.timer, 0, IMC_Client_Controller.scheduleList);
                }
                else if(clientType==1)//Change Advertisement Path for Generator
                {
                    System.out.println("Send M|Gene||");
                    out.println("M|Gene||");
                    out.flush();
                    String serverMsg;
                    String[] MsgArray;
                    serverMsg=in.readLine();
                    MsgArray = serverMsg.split("\\|");
                    System.out.println("Get Server Msg:\t" + serverMsg);
                    if (MsgArray[0].equals("O"))
                    {
                        if (MsgArray[1].equals("Gene"))
                        {
                            System.out.println("AD Path Change to :\t" + MsgArray[2]);
                            ADPathController.setADPath(MsgArray[2]);
                        }
                    }
                }
                else if(clientType==2)//Change Advertisement Path for Monitor UI
                {
                    System.out.println("Send M|Moni||");
                    out.println("M|Moni||");
                    out.flush();
                    String serverMsg;
                    String[] MsgArray;
                    serverMsg=in.readLine();
                    MsgArray = serverMsg.split("\\|");
                    System.out.println("Get Server Msg:\t" + serverMsg);
                    if (MsgArray[0].equals("O"))
                    {
                        if (MsgArray[1].equals("Moni"))
                        {
                            System.out.println("AD_M Path Change to :\t" + MsgArray[2]);
                            ADPathController.setAD_MPath(MsgArray[2]);
                        }
                    }
                }
                out.println("Z|||");//Disconnect
                out.flush();
                in.close();
                out.close();
                IMCserver.close();
            }

        }
        catch (IOException e)
        {
            System.out.println("Error: I/O has error");
        }
    }
}