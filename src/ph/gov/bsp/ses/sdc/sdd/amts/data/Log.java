package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class Log
{
	private static final String TABLE_NAME = "LOG";
	private static final String TABLE_COLS = "`ID`,`Folder`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`ApprovalStatus`,`AssignedBy`,`AssignedTo`,`AssignedOn`,`ProcessDetails`,`ProcessedBy`,`ProcessedOn`,`ResolvedOn`,`Remarks`,";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	private int id = -1;
	private String userId;
	private String action;
	private String effectedOn;
	private String tableName;
	private String fieldName;
	private int rowId = -1;
	private String oldValue;
	private String newValue;
	
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
		return getDate(effectedOn);
	}
	
	public void setEffectedOn(Date effectedOn)
	{
		this.effectedOn = formatDate(effectedOn);
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
	
	public String getOldValue()
	{
		return oldValue;
	}
	
	public void setOldValue(String oldValue)
	{
		this.oldValue = oldValue;
	}
	
	public String getNewValue()
	{
		return newValue;
	}
	
	public void setNewValue(String newValue)
	{
		this.newValue = newValue;
	}
	
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
	
	public static String formatDate(Date value)
	{
		if (value == null) return null;
		return DATE_FORMATTER.format(value);
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
			insert.setString(3, item.effectedOn);
			insert.setString(4, item.tableName);
			insert.setString(5, item.fieldName);
			insert.setInt(6, item.rowId);
			insert.setString(7, item.oldValue);
			insert.setString(8, item.newValue);
			
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
}
