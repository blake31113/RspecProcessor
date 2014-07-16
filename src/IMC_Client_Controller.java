import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Blake on 2014/6/10.
 */
public class IMC_Client_Controller
{
    private static IMC_Client_Controller instance = null;
    static final String filepath="./Procedure/Procedure.txt";
    static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
    public static ArrayList<ScheduleRecord> scheduleList=new ArrayList<ScheduleRecord>();
    public static Timer timer =new Timer();
    public static IMC_Client_Controller getInstance()//Singleton
    {
        if (instance == null)
        {
            instance = new IMC_Client_Controller();
        }
        return instance;
    }
    public static void main(String[] args) throws ParseException, IOException//for test
    {
        //IMC_Client_Controller_Initialize
        IMC_Client_Controller.IMC_Client_Controller_Initialize();
        //Schedule
        //IMC_Client_Controller.schedule(IMC_Client_Controller.timer, 0, IMC_Client_Controller.scheduleList);
        //Insert new Schedule
        //IMC_Client_Controller.insertSchedule("0|2014/06/24/13/56|./Request/140624.xml", IMC_Client_Controller.scheduleList);
        //IMC_Client_Controller.timer=IMC_Client_Controller.reSchedule(IMC_Client_Controller.timer, 0, IMC_Client_Controller.scheduleList);
        Timer askAD=new Timer();
        askAD.scheduleAtFixedRate(new IMC_Client(IMC_Client_Controller.scheduleList,0,1),0,2000);
        Timer askAD_M=new Timer();
        askAD_M.scheduleAtFixedRate(new IMC_Client(IMC_Client_Controller.scheduleList,0,2),0,2000);
    }
    public static void IMC_Client_Controller_Initialize() throws ParseException, IOException//Initialize Client Controller(Read,Sort,Rewrite)
    {
        System.out.println("IMC Client Controller Initialize");
        IMC_Client_Controller.readSchedule(IMC_Client_Controller.scheduleList);
        IMC_Client_Controller.sortSchedule(IMC_Client_Controller.scheduleList);
        IMC_Client_Controller.rewriteSchedule(IMC_Client_Controller.scheduleList);
    }
    public static void insertSchedule(String input, ArrayList<ScheduleRecord> myList) throws IOException, ParseException//Insert to Schedule and Sort
    {
        String[] str=input.split("\\|");
//        System.out.println(str[1]);
        if(df.parse(str[1]).compareTo(new Date())>0)
        {
            IMC_Client_Controller.addSchedule(myList, str[1], str[2]);
            sortSchedule(myList);
            rewriteSchedule(myList);
        }
    }
    public static Timer reSchedule(Timer t, int index, ArrayList<ScheduleRecord> myList) throws IOException//reSchedule
    {
        t.cancel();
        Timer newTimer=new Timer();
        newTimer.schedule(new IMC_Client(myList,index,0),myList.get(index).getDate());
        IMC_Client_Controller.printScheduleList();
        System.out.println("Schedule On Rails: "+myList.get(index).getfilename()+" will run at "+myList.get(index).getDate().toString());
        return newTimer;
    }
    public static void schedule(Timer t, int index, ArrayList<ScheduleRecord> myList) throws IOException//Schedule
    {
        if(myList.size()>0)
        {
            t.schedule(new IMC_Client(myList,index,0),myList.get(index).getDate());
            IMC_Client_Controller.printScheduleList();
            System.out.println("Schedule On Rails: "+myList.get(index).getfilename()+" will run at "+myList.get(index).getDate().toString());
        }
    }
    public static void printScheduleList()//Print Schedule List
    {
        for(int i=0;i<IMC_Client_Controller.scheduleList.size();i++)
        {

            System.out.println("Time: "+IMC_Client_Controller.scheduleList.get(i).getDate()+" File: "+IMC_Client_Controller.scheduleList.get(i).getfilename());
        }
    }
    public static void deleteScheduleByIndex(int i,ArrayList<ScheduleRecord> myList)//Delete Schedule when it is Expired
    {
        myList.remove(i);
    }
    public static void readSchedule(ArrayList<ScheduleRecord> myList) throws IOException, ParseException//read Schedule List from file
    {
        FileReader fr = new FileReader(filepath);
        BufferedReader br =new BufferedReader(fr);
        while(br.ready())
        {
            insertSchedule(br.readLine(),myList);
        }
    }
    public static void rewriteSchedule(ArrayList<ScheduleRecord> myList) throws IOException//Rewrite Schedule List to file
    {
        FileWriter fw = new FileWriter(filepath);
        String temp="";
        for(int i = 0; i < myList.size(); i++)
        {
            temp=temp+myList.get(i).getIndex()+"|"+df.format(myList.get(i).getDate())+"|"+myList.get(i).getfilename()+"\n";
        }
        fw.write(temp);
        fw.flush();
        fw.close();
    }
    public static void addSchedule(ArrayList<ScheduleRecord> myList, String date, String filename) throws ParseException//add Schedule to list
    {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        myList.add(new ScheduleRecord(df.parse(date),filename));
    }
    public static void sortSchedule(ArrayList<ScheduleRecord> myList)//sort Schedule list
    {
        Collections.sort(myList, new Comparator<ScheduleRecord>()
        {
            @Override
            public int compare(ScheduleRecord o1, ScheduleRecord o2)
            {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        for(int i=0;i<myList.size();i++)
        {
            myList.get(i).setIndex(i);
        }
    }

}
class ScheduleRecord//Schedule Structure
{
    private int index;
    private Date date;
    private String filename;
    public ScheduleRecord(Date inputDate,String inputName)
    {
        date=inputDate;
        filename=inputName;
    }
    public void setIndex(int inputIndex)
    {
        index=inputIndex;
    }
    public int getIndex()
    {
        return index;
    }
    public Date getDate()
    {
        return date;
    }
    public String getfilename()
    {
        return filename;
    }
}