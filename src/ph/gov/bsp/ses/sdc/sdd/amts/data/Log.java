package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Log
{
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
}
