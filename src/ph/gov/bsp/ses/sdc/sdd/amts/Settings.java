package ph.gov.bsp.ses.sdc.sdd.amts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Settings
{
	public static final String ASSIGNMENT_SYMBOL = "=";
	public static final String COMMENT_SYMBOL = ";";
	
	public static void main(String[] args)
	{
		Settings.get(new File("settings.ini"));
	}
	
	/**
	 * Constructs a {@link Settings} instance from the argument {@link File}. If 
	 * the file does not exist, this method creates one at the location specified 
	 * by the argument {@link File}.<br>
	 * <br>
	 * Any {@link IOException} instances encountered when reading or writing will 
	 * be caught but not acted upon, except for a call to {@link Exception#printStackTrace()}.
	 * @param file A file containing settings
	 * @return A {@link Settings} instance
	 */
	public static Settings get(File file)
	{
		Settings retobj = new Settings(file);
		
		if (file == null) throw new IllegalArgumentException("Argument File cannot be null");
		if (file.exists()) read(file, retobj);
		else write(file, retobj);
		
		return retobj;
	}
	
	private static void read(File file, Settings settings)
	{
		try
		{
			BufferedReader reader = null;
			
			try
			{
				reader = new BufferedReader(new FileReader(file));
				String line = null;
				
				while ((line = reader.readLine()) != null)
				{
					line = line.trim();
					
					if (line.isEmpty()) continue;
					
					if (line.startsWith(COMMENT_SYMBOL)) continue;
					
					if (line.contains(ASSIGNMENT_SYMBOL))
					{
						int assignment = line.indexOf(ASSIGNMENT_SYMBOL);
						if (assignment <= 0) continue; // Cannot set to empty setting name
						if (assignment >= (line.length() - 1)) continue; // Cannot set an empty value
						String setting = line.substring(0, assignment);
						String value = line.substring(assignment + 1, line.length());
						settings.set(setting, value, false);
					}
				}
			}
			finally
			{
				if (reader != null) reader.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void write(File file, Settings retobj)
	{
		try
		{
			PrintWriter printer = null;
			
			try
			{
				printer = new PrintWriter(file);
				
				printer.println("; This is a procedurally-generated file, manually editing this file is not recommended");
				
				for (String key : retobj.data.keySet())
				{
					if (retobj.defaults.containsKey(key))
					{
						printer.println(String.format("%s=%s", key, retobj.get(key)));
					}
				}
				
				printer.println("; " + new SimpleDateFormat("yyyy-MM-dd HH:mm.ss").format(new Date(System.currentTimeMillis())));
			}
			finally
			{
				if (printer != null) printer.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private File file;
	private HashMap<String, String> data;
	private HashMap<String, String> defaults;
	
	private Settings()
	{
		data = new HashMap<String, String>();
		
		defaults = new HashMap<String, String>();
		defaults.put("path.sqlitedb", "amts.sqlite3");
		defaults.put("path.fileserver", ".\\");
		defaults.put("path.localcopy", ".\\");
		defaults.put("data.busytimeoutmsecs", "30000");
		defaults.put("data.mkdirtries", "100");
		defaults.put("data.displaydelta", "19");
		defaults.put("ui.mainwindow.windowstate", "RESTORED");
		defaults.put("ui.receivingdialog.windowstate", "RESTORED");
		defaults.put("ui.assignmentdialog.windowstate", "RESTORED");
		defaults.put("ui.processingdialog.windowstate", "RESTORED");
		defaults.put("filter.receivingcomposite.requesttype", "");
		defaults.put("filter.receivingcomposite.requestedby", "");
		defaults.put("filter.assignmentcomposite.status", "NEW");
		defaults.put("filter.assignmentcomposite.requesttype", "");
		defaults.put("filter.assignmentcomposite.requestedby", "");
		defaults.put("filter.processingcomposite.status", "APPROVED");
		defaults.put("filter.processingcomposite.requesttype", "");
		defaults.put("filter.processingcomposite.assignedto", "");
	}
	
	private Settings(File file)
	{
		this();
		if (file == null) throw new IllegalArgumentException("Argument File cannot be null");
		this.file = file;
	}
	
	public String get(String setting)
	{
		if (setting == null) throw new IllegalArgumentException("Argument String cannot be null");
		setting = setting.toLowerCase();
		setting = setting.replaceAll("\\s+", "");
		if (data.containsKey(setting)) return data.get(setting);
		else return getDefault(setting);
	}
	
	public String getDefault(String setting)
	{
		if (setting == null) throw new IllegalArgumentException("Argument String cannot be null");
		setting = setting.toLowerCase();
		setting = setting.replaceAll("\\s+", "");
		if (defaults.containsKey(setting)) return defaults.get(setting);
		else throw new IllegalArgumentException("Setting \"" + setting + "\" is not defined");
	}
	
	private synchronized void set(String setting, String value, boolean write)
	{
		if (setting == null) throw new IllegalArgumentException("Argument String cannot be null");
		setting = setting.toLowerCase();
		setting = setting.replaceAll("\\s+", "");
		if (value == null) value = "";
		data.put(setting.toLowerCase(), value);
		if (write) write(this.file, this);
	}
	
	public void set(String setting, String value)
	{
		set(setting, value, true);
	}
}
