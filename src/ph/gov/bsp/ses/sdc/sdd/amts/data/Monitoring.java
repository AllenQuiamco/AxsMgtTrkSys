package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

public class Monitoring implements Cloneable
{
	private static final String TABLE_NAME = "MONITORING";
	private static final String TABLE_COLS = "`ID`,`Folder`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`ApprovalStatus`,`AssignedBy`,`AssignedTo`,`AssignedOn`,`ProcessDetails`,`ProcessedBy`,`ProcessedOn`,`ResolvedOn`,`Remarks`";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	private int id = -1; // 0 is test, any positive is actual data
	private String folder;
	private String requestType;
	private String requestedBy;
	private String receivedOn;
	private String receivedBy;
	private String approvalStatus;
	private String assignedBy;
	private String assignedTo;
	private String assignedOn;
	private String processDetails;
	private String processedBy;
	private String processedOn;
	private String resolvedOn;
	private String remarks;
	
	@Override
	public Monitoring clone()
	{
		Monitoring clone = new Monitoring();
		
		clone.id = this.id;
		clone.folder = this.folder;
		clone.requestType = this.requestType;
		clone.requestedBy = this.requestedBy;
		clone.receivedOn = this.receivedOn;
		clone.receivedBy = this.receivedBy;
		clone.approvalStatus = this.approvalStatus;
		clone.assignedBy = this.assignedBy;
		clone.assignedTo = this.assignedTo;
		clone.assignedOn = this.assignedOn;
		clone.processDetails = this.processDetails;
		clone.processedBy = this.processedBy;
		clone.processedOn = this.processedOn;
		clone.resolvedOn = this.resolvedOn;
		clone.remarks = this.remarks;
		
		return clone;
	}
	
	public int getID()
	{
		return id;
	}

	public void setID(int id)
	{
		this.id = id;
	}

	public String getFolder()
	{
		return folder;
	}

	public void setFolder(String folder)
	{
		this.folder = folder;
	}

	public String getRequestType()
	{
		return requestType;
	}

	public void setRequestType(String requestType)
	{
		this.requestType = requestType;
	}

	public String getRequestedBy()
	{
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy)
	{
		this.requestedBy = requestedBy;
	}

	public Date getReceivedOn()
	{
		return getDate(receivedOn);
	}
	
	public void setReceivedOn(Date receivedOn)
	{
		this.receivedOn = formatDate(receivedOn);
	}

	public String getReceivedBy()
	{
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy)
	{
		this.receivedBy = receivedBy;
	}

	public String getApprovalStatus()
	{
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}

	public String getAssignedBy()
	{
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy)
	{
		this.assignedBy = assignedBy;
	}

	public String getAssignedTo()
	{
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo)
	{
		this.assignedTo = assignedTo;
	}

	public Date getAssignedOn()
	{
		return getDate(assignedOn);
	}

	public void setAssignedOn(Date assignedOn)
	{
		this.assignedOn = formatDate(assignedOn);
	}

	public String getProcessDetails()
	{
		return processDetails;
	}

	public void setProcessDetails(String processDetails)
	{
		this.processDetails = processDetails;
	}

	public String getProcessedBy()
	{
		return processedBy;
	}

	public void setProcessedBy(String processedBy)
	{
		this.processedBy = processedBy;
	}

	public Date getProcessedOn()
	{
		return getDate(processedOn);
	}

	public void setProcessedOn(Date processedOn)
	{
		this.processedOn = formatDate(processedOn);
	}

	public Date getResolvedOn()
	{
		return getDate(resolvedOn);
	}

	public void setResolvedOn(Date resolvedOn)
	{
		this.resolvedOn = formatDate(resolvedOn);
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
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
	
	@SuppressWarnings("deprecation")
	public static LinkedList<Monitoring> getSamples(int max)
	{
		LinkedList<Monitoring> retobj = new LinkedList<Monitoring>();
		
		Random random = new Random();
		Monitoring item; 
		//int max = new Random().nextInt(10) + 1;		
		
		for(int i = 0; i < max; i++)
		{
			item = new Monitoring();
			item.id = random.nextInt(100);
			item.folder = String.format("folder%08x", random.nextInt());
			item.requestType = String.format("requestType%08x", random.nextInt());
			item.requestedBy = String.format("requestedBy%08x", random.nextInt());
			item.setReceivedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.receivedBy = String.format("receivedBy%08x", random.nextInt());
			item.approvalStatus = String.format("approvalStatus%08x", random.nextInt());
			item.assignedBy = String.format("assignedBy%08x", random.nextInt());
			item.assignedTo = String.format("assignedTo%08x", random.nextInt());
			item.setAssignedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.processDetails = String.format("processDetails%08x", random.nextInt());
			item.processedBy = String.format("processedBy%08x", random.nextInt());
			item.setProcessedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.setResolvedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.remarks = String.format("remarks%08x", random.nextInt());
			retobj.add(item);
		}
		
		return retobj;
	}

	public static int queryRowsReceiving(Connection conn, String filterType, String filterFrom) throws SQLException
	{
		String where = ""; // TODO process filters
		
		return queryRows(conn, where);
	}

	private static int queryRows(Connection conn, String where) throws SQLException
	{
		Statement select = conn.createStatement();
		ResultSet result = select.executeQuery(String.format("SELECT COUNT(*) FROM %s %s", TABLE_NAME, where));
		return result.getInt(1);
	}

	public static List<Monitoring> getRowsReceiving(Connection conn, String filterType, String filterFrom, int rowStart, int rowEnd) throws SQLException
	{
		String where = ""; // TODO process filters
		
		return getRows(conn, where, rowStart, rowEnd);
	}

	private static List<Monitoring> getRows(Connection conn, String where, int rowStart, int rowEnd) throws SQLException
	{
		int limit = (rowEnd - rowStart) + 1;
		int offset = rowStart - 1;
		String page = String.format("LIMIT %d OFFSET %d", limit, offset);
		
		String orderBy = "ORDER BY `ID` DESC";
		
		return getRows(conn, where, orderBy, page);
	}

	private static List<Monitoring> getRows(Connection conn, String where, String orderBy, String page) throws SQLException
	{
		Statement select = conn.createStatement();
		ResultSet result = select.executeQuery(String.format("SELECT %s FROM %s %s %s %s", TABLE_COLS, TABLE_NAME, where, orderBy, page));
		
		List<Monitoring> retobj = new LinkedList<Monitoring>();
		
		while (result.next())
		{
			Monitoring item = new Monitoring();
			
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

	/**
	 * <p>WARNING: This method calls {@link PreparedStatement#executeUpdate()}. 
	 * Database commit or rollback operations must be done outside of this 
	 * method.</p>
	 */
	public static void addNew(Connection conn, Monitoring item) throws SQLException
	{
		PreparedStatement insert = null;
		
		try
		{
			String insertNew = String.format("INSERT INTO %s ", TABLE_NAME);
			insertNew += "(`Folder`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`ApprovalStatus`,`Remarks`) ";
			insertNew += "VALUES (?,?,?,?,?,?,?)";
			insert = conn.prepareStatement(insertNew);
			insert.setString(1, item.folder);
			insert.setString(2, item.requestType);
			insert.setString(3, item.requestedBy);
			insert.setString(4, item.receivedOn);
			insert.setString(5,  item.receivedBy);
			insert.setString(6, item.approvalStatus);
			insert.setString(7, item.remarks);
			
			int result = insert.executeUpdate();
			if (result < 1) throw new SQLException("No rows were inserted.");
			else if (result > 1) throw new SQLException("More than one row was inserted.");
		}
		finally
		{
			if (insert != null) insert.close(); 
		}
	}

	public static int getIdFromFolder(Connection conn, String folder) throws SQLException
	{
		int retval = 0;
		
		String selectString = String.format("SELECT `ID` FROM %s WHERE `FOLDER`=?", TABLE_NAME);
		PreparedStatement select = conn.prepareStatement(selectString);
		select.setString(1, folder);
		ResultSet result = select.executeQuery();
		if (result.next())
		{
			retval = result.getInt(1);
		}
		
		return retval;
	}

	public static Monitoring getItem(Connection conn, int id) throws SQLException
	{
		List<Monitoring> rows = getRows(conn, "WHERE `ID`=" + id, "", "");
		
		if (rows.size() == 1) return rows.get(0);
		else if (rows.size() == 0) return null;
		else throw new SQLException("Too many rows for ID=" + id);
	}

	public static void update(Connection conn, List<Log> logs) throws SQLException
	{
		for (Log log : logs)
		{
			if (!Utilities.equals("update", log.getAction())) throw new SQLException("Invalid LOG.Action: " + log.getAction());
			if (!Utilities.equals(TABLE_NAME, log.getTableName())) throw new SQLException("Wrong table name: " + log.getTableName());
			
			String updateString = String.format("UPDATE %s SET `%s`=? WHERE `ID`=?", TABLE_NAME, log.getFieldName());
			PreparedStatement update = null;
			try
			{
				update = conn.prepareStatement(updateString);
				update.setString(1, log.getNewValue());
				update.setInt(2, log.getRowId());
				
				int result = update.executeUpdate();
				if (result < 1) throw new SQLException("No rows were inserted.");
				else if (result > 1) throw new SQLException("More than one row was updated.");
			}
			finally
			{
				if (update != null) update.close();
			}
		}
	}
	
}
