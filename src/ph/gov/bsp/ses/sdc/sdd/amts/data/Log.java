package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

public class Log
{
	private static final String TABLE_COLS = "`ID`,`UserID`,`Action`,`EffectedOn`,`TableName`,`FieldName`,`RowID`,`OldValue`,`NewValue`";
	private static final String TABLE_NAME = "LOG";
	
	private int id = -1;
	private String userId;
	private String action;
	private Long effectedOn;
	private String tableName;
	private String fieldName;
	private int rowId = -1;
	private Object oldValue;
	private Object newValue;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public void setAction(String action)
	{
		this.action = action;
	}
	
	public Date getEffectedOn()
	{
		return Common.getDate(effectedOn);
	}
	
	public void setEffectedOn(Date effectedOn)
	{
		//this.effectedOn = formatDate(effectedOn);
		this.effectedOn = Common.morphDate(effectedOn);
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
	
	public String getFieldName()
	{
		return fieldName;
	}
	
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	
	public int getRowId()
	{
		return rowId;
	}
	
	public void setRowId(int rowId)
	{
		this.rowId = rowId;
	}
	
	public Object getOldValue()
	{
		return oldValue;
	}
	
	public void setOldValue(Object oldValue)
	{
		this.oldValue = oldValue;
	}
	
	public Object getNewValue()
	{
		return newValue;
	}
	
	public void setNewValue(Object newValue)
	{
		this.newValue = newValue;
	}
	
	private static List<Log> getRows(Connection conn, String where, String orderBy, String page) throws SQLException
	{
		Statement select = conn.createStatement();
		ResultSet result = select.executeQuery(String.format("SELECT %s FROM %s %s %s %s", TABLE_COLS, TABLE_NAME, where, orderBy, page));
		
		List<Log> retobj = new LinkedList<Log>();
		
		while (result.next())
		{
			Log item = new Log();
			
			for (Field field : item.getClass().getDeclaredFields())
			{
				// append only non-static public fields
				boolean collect = true;
				int modifiers = field.getModifiers();
				//collect &= (modifiers & Modifier.PUBLIC) != 0; // is public
				collect &= (modifiers & Modifier.STATIC) == 0; // not static
				collect &= (modifiers & Modifier.FINAL) == 0; // not final
				
				if (!collect) continue;
				
				try
				{
					field.set(item, result.getObject(field.getName()));
				}
				catch (IllegalArgumentException e)
				{
					String check = "Can not set field to null value";
					check = check.trim().toUpperCase().replaceAll("", ".*");
					if (!e.getMessage().toUpperCase().matches(check)) e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
			
			retobj.add(item);
		}
		
		return retobj;
	}
	
	public static void append(Connection conn, Log item, String user, Date date) throws SQLException
	{
		PreparedStatement insert = null;
		
		try
		{
			item.setEffectedOn(date);
			item.setUserId(user);
			
			String insertNew = String.format("INSERT INTO %s ", TABLE_NAME);
			insertNew += "(`UserID`,`Action`,`EffectedOn`,`TableName`,`FieldName`,`RowID`,`OldValue`,`NewValue`) ";
			insertNew += "VALUES (?,?,?,?,?,?,?,?)";
			insert = conn.prepareStatement(insertNew);
			insert.setString(1, item.userId);
			insert.setString(2, item.action);
			insert.setLong(3, item.effectedOn);
			insert.setString(4, item.tableName);
			insert.setString(5, item.fieldName);
			insert.setInt(6, item.rowId);
			insert.setObject(7, item.oldValue);
			insert.setObject(8, item.newValue);
			
			int result = insert.executeUpdate();
			if (result < 1) throw new SQLException("No rows were inserted.");
			else if (result > 1) throw new SQLException("More than one row was inserted.");
		}
		finally
		{
			if (insert != null) insert.close();
		}
	}

	public static void append(Connection conn, List<Log> logs, String user, Date date) throws SQLException
	{
		for (Log log : logs)
		{
			append(conn, log, user, date);
		}
	}
	
	public static boolean createOutputRaw(Connection conn, Filter filter, PrintWriter pw, boolean append) throws SQLException
	{
		boolean success = false;
		String header = TABLE_COLS.replace("`", "\"");
		String where = "";
		if (filter != null) 
		{
			String filterBuild = filter.build();
			if (!Utilities.isNullOrBlank(filterBuild)) where = "WHERE " + filterBuild;
		}
		
		List<Log> items = getRows(conn, where, "", "");
		if (items.size() > 0)
		{
			if (!append) pw.println(header);
			
			for (Log item : items)
			{
				write(item, pw);
			}
			
			success = true;
		}
		
		return success;
	}
	
	private static void write(Log item, PrintWriter pw)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("%d", item.getId())); 
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getUserId())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getAction())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvDate(item.getEffectedOn())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getTableName())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getFieldName())));
		sb.append(",");
		sb.append(String.format("%d", item.getRowId())); 
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getOldValue())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getNewValue())));
		
		pw.println(sb.toString());
	}
}
