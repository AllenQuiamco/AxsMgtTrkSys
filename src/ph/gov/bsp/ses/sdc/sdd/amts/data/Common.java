package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Common
{
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	public static Date getDate(String value)
	{
		Date retobj = null;
		if (value == null) return retobj;
		try
		{
			java.util.Date date = DATE_FORMATTER.parse(value);
			retobj = new Date(date.getTime());
		}
		catch (ParseException pe)
		{
			pe.printStackTrace();
		}
		return retobj;
	}
	
	public static Date getDate(Long value) // yyyyMMddHHmmss
	{
		if (value == null) return null;
		
		long year = value / 10000000000L;
		value = value % 10000000000L;
		
		long month =  value / 100000000L;
		value = value % 100000000L;
		
		long day = value / 1000000L;
		value = value % 1000000L;
		
		long hour = value / 10000L;
		value = value % 10000L;
		
		long mins = value / 100;
		value = value % 100;
		
		long secs = value;
		
		return getDate(String.format("%d-%d-%d %d:%d:%d", year, month, day, hour, mins, secs));
	}
	
	public static String formatDate(Date value)
	{
		if (value == null) return null;
		return DATE_FORMATTER.format(value);
	}
	
	public static Long morphDate(Date value)
	{
		if (value == null) return null;
		
		String formattedDate = formatDate(value);
		formattedDate = formattedDate.replace("-", "").replace(" ", "").replace(":", "");
		return Long.parseLong(formattedDate);
	}

	public static Long morphDate(int year, int month, int day)
	{		
		return (year * 10000000000L) + ((month + 1) * 100000000L) + (day * 1000000L);
	}

	public static String cleanColumnName(String column)
	{
		if (column == null) throw new IllegalArgumentException("Argument String cannot be null");
		return column.replace("`", "")
				.replace(";", "")
				.replace("-", "")
				.replace(" ", "");
	}
	
	public static String cleanSqlValue(String string)
	{
		// TODO Implement this
		return string;
	}

	public static String morphToCsvString(Object string)
	{
		if (string == null) return null;
		return string.toString();
	}
	
	public static String morphToCsvString(String string)
	{
		if (string == null) return "";
		
		string = string
				.replace("\n", " ")
				.replace("\r", "")
				.replace("\"", "\"\"");
		
		return String.format("\"%s\"", string);
	}

	public static String morphToCsvDate(Date date)
	{
		if (date == null) return "";
		
		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy hh:mm:ss a");
		return formatter.format(date);
	}
}
