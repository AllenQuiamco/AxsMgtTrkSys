package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

public class Filter
{
	List<String> parts = new LinkedList<String>();
	
	@SuppressWarnings("deprecation")
	public void add(String column, Date from, Date to)
	{
		column = Common.cleanColumnName(column);
		
		Calendar cal = GregorianCalendar.getInstance();
		
		if ((from == null) && (to == null)) return;
		
		else if ((from != null) && (to == null))
		{
			cal.setTime(from);
			String part = String.format("%s>=%d", column, Common.morphDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)));
			parts.add(part);
		} 
		else if ((from == null) && (to != null)) 
		{
			cal.setTime(to);
			cal.roll(Calendar.DAY_OF_YEAR, true);
			String part = String.format("%s<%d", column, Common.morphDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)));
			parts.add(part);
		}
		else //if ((from != null) && (to != null))
		{
			if (from.compareTo(to) > 0)
			{
				Date temp = from;
				from = to;
				to = temp;
			}
			
			Calendar calFrom = (Calendar)cal.clone();
			calFrom.setTime(from);
			cal.setTime(to);
			cal.roll(Calendar.DAY_OF_YEAR, true);
			String part = String.format("(%s>=%d) AND (%s<%d)", 
					column, Common.morphDate(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH), calFrom.get(Calendar.DATE)), 
					column, Common.morphDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)));
			parts.add(part);
		}
	}
	
	public void add(String column, String _like_)
	{
		if (Utilities.isNullOrBlank(_like_)) return;
		column = Common.cleanColumnName(column);
		_like_ = Common.cleanSqlValue(_like_);
		String part = String.format("%s LIKE '%%%s%%'", column, _like_);
		parts.add(part);
	}
	
	public String build()
	{
		StringBuilder sb = new StringBuilder();
		
		for (String part : parts)
		{
			if (sb.length() > 0) sb.append(" AND ");
			sb.append(String.format("(%s)", part));
		}
		
		return sb.toString();
	}
}
