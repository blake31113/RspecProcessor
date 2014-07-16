/**
 * Created by Blake on 2014/6/10.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DateTask extends TimerTask
{
    private String filename;
    public DateTask(String input)
    {
        filename=input;
    }
    @ Override
    public void run()
    {
        System.out.println("Task 執行時間：" + new Date());
        FileReader fr = null;
        try
        {
            fr = new FileReader("./Request/"+filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br =new BufferedReader(fr);
        try {
            while(br.ready())
            {
                System.out.println(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}