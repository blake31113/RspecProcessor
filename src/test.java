import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Blake on 2014/5/23.
 */
public class test
{
    static String MonitorCommand(String OPcode,String var1,String var2,String var3)
    {
        String command="";
        command=OPcode+"|"+var1+"|"+var2+"|"+var3;
        return  command;
    }
    public static void main(String[] args) throws UnknownHostException
    {
        //欲切割的字串
        String splitString = "A|asdf|qwer|zxcv";
        //使用「:」進行切割
        String[] names = splitString.split("\\|");
        for(String name:names)
        {
            System.out.println(name);
        }

    }
}
