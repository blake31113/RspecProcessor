import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

public class ConfigReader 
{
	private static final String FILEPATH_STRING = "./config/config.conf"; //configuration file name
	private Conf conf;
	private void parserConigFile(String name, String value) 
	{
        // TODO Auto-generated method stub
        if (name.equals("monitor_ip"))
        {
            conf.monitor_ip = value;
        }
        else if (name.equals("nics"))
        {
            conf.nics = value;
        }
        else if (name.equals("monitor_port"))
        {
            conf.monitor_port = Integer.parseInt(value);
        }
        else if (name.equals("hostName"))
        {
                conf.hostName = value;

        }
	}
	
	public ConfigReader()
	{
		conf = new Conf();
	}
	
	
	public Conf getConf()
	{
		File configFile = new File(FILEPATH_STRING);
		try 
		{
			
			BufferedReader reader= new BufferedReader(new FileReader(configFile));
			String line = null;
			while((line = reader.readLine()) !=null)
			{
				try 
				{
					String non_comment = line.replaceAll(" ", "").toLowerCase().split("#")[0];
					if(non_comment.length()==0)
						continue;
					parserConigFile(non_comment.split("=")[0],non_comment.split("=")[1]);
				} 
				catch (ArrayIndexOutOfBoundsException e) 
				{
					// TODO: handle exception
					e.printStackTrace();
				}
				catch (PatternSyntaxException e) 
				{
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conf;
	}



	
	
}
