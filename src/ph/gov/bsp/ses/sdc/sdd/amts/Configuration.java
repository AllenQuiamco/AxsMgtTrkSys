package ph.gov.bsp.ses.sdc.sdd.amts;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

public class Configuration implements Serializable, Cloneable
{	
	private static final long serialVersionUID = 2785454170415068943L;
	
	public static void main(String[] args)
	{
		File file = getFile();
		System.out.println("filepath := " + file.getAbsolutePath());
		Configuration config = new Configuration();
		
		// test write
		try
		{
			Configuration.write(file, config);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// test clone and toString
		try
		{
			Configuration clone = (Configuration) config.clone();
			System.out.println(clone.toString());
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
	}
	
	public static final String FILE_NAME = "config.dat";
	public static final String APP_NAME = "amaterasu";
	
	public static File getFile()
	{
		String filedir = System.getenv("APPDATA");
		if (!new File(filedir).exists()) filedir = System.getenv("USERPROFILE");
		else if (!new File(filedir).exists()) filedir = System.getenv("USER_HOME");
		else if (!new File(filedir).exists()) filedir = "";
		
		if (!filedir.equals(""))
		{ // load from user dir
			File file = new File(filedir, APP_NAME);
			if (!file.exists()) file.mkdir();
			filedir = file.getAbsolutePath();
		}
		
		File file = new File(filedir, Configuration.FILE_NAME);
		return file;
	}
	
	public static Configuration read(File file) throws IOException, ClassNotFoundException
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try
		{
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			return (Configuration) ois.readObject();
		}
		finally
		{
			if (ois != null) ois.close();
			if (fis != null) fis.close();
		}
	}
	
	public static void write(File file, Configuration config) throws IOException
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try
		{
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(config);
		}
		finally
		{
			if (oos != null) oos.close();
			if (fos != null) fos.close();
		}
	}
	
	public final int mkdirTries = 100;
	public String SqliteDbFilePath = "amts.sqlite3";
	public String FileServerPath = ".\\";
	public String LocalCopyPath = ".\\";
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Configuration retobj = new Configuration();
		
		for (Field field : this.getClass().getDeclaredFields())
		{
			try
			{
				// do not set final, static or transient members
				boolean set = true;
				int modifiers = field.getModifiers();
				set &= (modifiers & Modifier.FINAL) == 0; // not final
				set &= (modifiers & Modifier.STATIC) == 0; // not static
				set &= (modifiers & Modifier.TRANSIENT) == 0; // not transient
				if (!set) continue;
				
				field.set(retobj, field.get(this));
			}
			catch (IllegalArgumentException e)
			{
				// throw new RuntimeException("Unable to clone instance.", e);
				continue;
			}
			catch (IllegalAccessException e)
			{
				// throw new RuntimeException("Unable to clone instance.", e);
				continue;
			}
		}
		
		return retobj;
	}
	
	@Override
	public String toString()
	{
		return Utilities.dump(this, null);
	}
	
	public String getSqliteConnectionString()
	{
		return getSqliteConnectionString(this.SqliteDbFilePath);
	}
	
	public static String getSqliteConnectionString(String filepath)
	{
		return String.format("jdbc:sqlite:%s", filepath.replace("\\", "/"));
	}
}
