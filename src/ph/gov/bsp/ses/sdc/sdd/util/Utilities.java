package ph.gov.bsp.ses.sdc.sdd.util;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Contains static methods useful for various purposes.
 * 
 * @author © 2017 Miguel Aguinaldo
 */
public final class Utilities
{
	
	/**
	 * This class cannot be instantiated.
	 */
	private Utilities()
	{
		
	}
		
	public static String dump(Object o, PrintStream out)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("[%s(%08X) ", o.getClass().getSimpleName(), o.hashCode()));
		
		for (Field field : o.getClass().getDeclaredFields())
		{
			// append only non-static public fields
			boolean append = true;
			int modifiers = field.getModifiers();
			append &= (modifiers & Modifier.STATIC) == 0; // not static
			append &= (modifiers & Modifier.PUBLIC) != 0; // is public
			
			if (!append) continue;
			
			String value = "";
			
			try
			{
				value = field.get(o).toString();
			}
			catch (IllegalArgumentException e)
			{
				value = String.format("![%s]", e.getClass().getSimpleName());
			}
			catch (IllegalAccessException e)
			{
				value = String.format("![%s]", e.getClass().getSimpleName());
			}
			
			// enclose value in double quotes if String
			if (field.getType().getName().equals("java.lang.String")) value = String.format("\"%s\"", value);
			
			sb.append(String.format("%s:=%s ", field.getName(), value));
		}
		
		sb.append("]");
		
		try
		{
			if (out != null)
			{
				out.printf("%s%n", sb.toString());
				out.flush();
			}
		}
		catch (Throwable t)
		{
			// ignore any printing error
		}
		
		return sb.toString();
	}

	public static void dumpenv(PrintStream out)
	{
		Map<String, String> env = System.getenv();
		for(String key : env.keySet())
		{
			out.printf("%s := %s%n", key, env.get(key));
		}
	}
	
	public static String getAbsolutePath(String path)
	{
		return new File(path).getAbsolutePath();
	}

	public static String[] toArray(String ... strings)
	{
		return strings;
	}

	public static boolean equals(String s1, String s2)
	{
		if (s1 == null && s2 == null) return true;
		else if (s1 != null && s2 == null) return false;
		if (s1 == null && s2 != null) return false;
		else return s1.equals(s2);
	}
}
