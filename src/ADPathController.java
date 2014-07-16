import java.io.*;

/**
 * Created by Blake on 2014/6/24.
 */
public class ADPathController
{
    private static ADPathController instance = null;
    private static FileReader fr;
    public static String Gene_Path;
    public static String Moni_Path;
    private static String configPath="./config/config.txt";
    public static ADPathController getInstance() throws FileNotFoundException//Singleton
    {
        if (instance == null)
        {
            instance = new ADPathController();
        }
        return instance;
    }
    public ADPathController() throws FileNotFoundException
    {
        ADPathController.readADPath();
    }
//    public static void main(String[] args) throws IOException //for test
//    {
//        ADPathController.readADPath();
//        ADPathController.setADPath("asdf.xml");//./Advertisement/Advertisement.xml
//        ADPathController.setAD_MPath("fads.xml");//./Advertisement/Advertisement_M.xml
//    }
    public static void readADPath() throws FileNotFoundException//Read Advertisement Path
    {
        fr = new FileReader(ADPathController.configPath);
        BufferedReader br =new BufferedReader(fr);
        try
        {
            while(br.ready())
            {
                String temp=br.readLine();
                if(temp.charAt(0)=='#')
                {
                    ADPathController.Gene_Path=temp.substring(1);
                    System.out.println("Now AD at: "+ADPathController.Gene_Path);
                }
                else if(temp.charAt(0)=='@')
                {
                    ADPathController.Moni_Path=temp.substring(1);
                    System.out.println("Now AD at: "+ADPathController.Moni_Path);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void setADPath(String path) throws IOException//set Advertisement Path
    {
        ADPathController.Gene_Path=path;
        ADPathController.rewriteADPath();
    }
    public static void setAD_MPath(String path) throws IOException//set Advertisement for Monitor Path
    {
        ADPathController.Moni_Path=path;
        ADPathController.rewriteADPath();
    }
    public static void rewriteADPath() throws IOException//rewrite Advertisements Path
    {
        FileWriter fw = new FileWriter(ADPathController.configPath);
        fw.write("#"+ADPathController.Gene_Path+"\n");
        fw.write("@"+ADPathController.Moni_Path+"\n");
        fw.flush();
        fw.close();
    }

}


