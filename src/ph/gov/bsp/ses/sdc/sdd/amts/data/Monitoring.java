package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

public class Monitoring implements Cloneable
{
	private static final String TABLE_NAME = "MONITORING";
	private static final String TABLE_COLS = "`ID`,`Folder`,`EnteredOn`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`Status`,`AssignedBy`,`AssignedTo`,`AssignedOn`,`ProcessedBy`,`ProcessedOn`,`ResolvedOn`,`Remarks`";
		
	private int id = -1; // 0 is test, any positive is actual data
	private String folder;
	private Long enteredOn;
	private String requestType;
	private String requestedBy;
	private Long receivedOn;
	private String receivedBy;
	private String status;
	private String assignedBy;
	private String assignedTo;
	private Long assignedOn;
	private String processedBy;
	private Long processedOn;
	private Long resolvedOn;
	private String remarks;
	
	@Override
	public Monitoring clone()
	{
		Monitoring clone = new Monitoring();
		
		clone.id = this.id;
		clone.folder = this.folder;
		clone.enteredOn = this.enteredOn;
		clone.requestType = this.requestType;
		clone.requestedBy = this.requestedBy;
		clone.receivedOn = this.receivedOn;
		clone.receivedBy = this.receivedBy;
		clone.status = this.status;
		clone.assignedBy = this.assignedBy;
		clone.assignedTo = this.assignedTo;
		clone.assignedOn = this.assignedOn;
		clone.processedBy = this.processedBy;
		clone.processedOn = this.processedOn;
		clone.resolvedOn = this.resolvedOn;
		clone.remarks = this.remarks;
		
		return clone;
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
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

	public Date getEnteredOn()
	{
		return Common.getDate(enteredOn);
	}

	public void setEnteredOn(Date value)
	{
		this.enteredOn = Common.morphDate(value);
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
		return Common.getDate(receivedOn);
	}
	
	public void setReceivedOn(Date receivedOn)
	{
		//this.receivedOn = formatDate(receivedOn);
		this.receivedOn = Common.morphDate(receivedOn);
	}

	public String getReceivedBy()
	{
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy)
	{
		this.receivedBy = receivedBy;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
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
		return Common.getDate(assignedOn);
	}

	public void setAssignedOn(Date assignedOn)
	{
		//this.assignedOn = formatDate(assignedOn);
		this.assignedOn = Common.morphDate(assignedOn);
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
		return Common.getDate(processedOn);
	}

	public void setProcessedOn(Date processedOn)
	{
		//this.processedOn = formatDate(processedOn);
		this.processedOn = Common.morphDate(processedOn);
	}

	public Date getResolvedOn()
	{
		return Common.getDate(resolvedOn);
	}

	public void setResolvedOn(Date resolvedOn)
	{
		//this.resolvedOn = formatDate(resolvedOn);
		this.resolvedOn = Common.morphDate(resolvedOn);
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
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
			item.setEnteredOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.requestType = String.format("requestType%08x", random.nextInt());
			item.requestedBy = String.format("requestedBy%08x", random.nextInt());
			item.setReceivedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.receivedBy = String.format("receivedBy%08x", random.nextInt());
			item.status = String.format("status%08x", random.nextInt());
			item.assignedBy = String.format("assignedBy%08x", random.nextInt());
			item.assignedTo = String.format("assignedTo%08x", random.nextInt());
			item.setAssignedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.processedBy = String.format("processedBy%08x", random.nextInt());
			item.setProcessedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.setResolvedOn(new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1));
			item.remarks = String.format("remarks%08x", random.nextInt());
			retobj.add(item);
		}
		
		return retobj;
	}

	private static String buildWhere(String ... strings) // TODO Transfer method to Common
	{
		if ((strings.length % 2) != 0) throw new IllegalArgumentException("Parameter array does not have an even number of elements.");
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < strings.length; i += 2)
		{
			String column = strings[i];
			String filter = strings[i + 1];
			
			String part = buildWhere(column, filter);
			
			if (Utilities.isNullOrBlank(part)) continue;
			
			if (sb.length() > 0) sb.append(" AND ");
			
			sb.append(String.format("(%s)",part));
		}
		
		if (sb.length() > 0) sb.insert(0, "WHERE ");
		
		return sb.toString();
	}
	
	private static String buildWhere(String column, String filter) // TODO Transfer method to Common
	{
		if (Utilities.isNullOrBlank(column)) return "";
		if (Utilities.isNullOrBlank(filter)) return "";
		
		column = column.replace("'", "").replace("\"", "").replace("-", "").replace(";", "");
		filter = filter.replace("'", "").replace("\"", "").replace("-", "").replace(";", "");
		
		String[] filters = null;
		
		if (filter.contains("|")) filters = filter.split("|");
		else filters = new String[] { filter };
		
		StringBuilder sb = new StringBuilder();
		
		for (String filterItem : filters)
		{
			if (Utilities.isNullOrBlank(filterItem)) continue;
			if (sb.length() > 0) sb.append(" OR ");
			sb.append(String.format("%s LIKE '%%%s%%'", column, filter));
		}
		
		return sb.toString();
	}

	public static int queryRowsReceiving(Connection conn, String filterType, String filterFrom) throws SQLException
	{
		String where = buildWhere(Utilities.toArray("RequestType", filterType, "RequestedBy", filterFrom));
		
		return queryRows(conn, where);
	}

	private static int queryRows(Connection conn, String where) throws SQLException
	{
		Statement select = null;
		try 
		{
			select = conn.createStatement();
			ResultSet result = select.executeQuery(String.format("SELECT COUNT(*) FROM %s %s", TABLE_NAME, where));
			return result.getInt(1);
		}
		finally
		{
			if (select != null) select.close();
		}
	}

	public static List<Monitoring> getRowsReceiving(Connection conn, String filterType, String filterFrom, int rowStart, int rowEnd) throws SQLException
	{
		String where = buildWhere("RequestType", filterType, "RequestedBy", filterFrom);
		
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
			insertNew += "(`Folder`,`EnteredOn`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`Status`,`Remarks`) ";
			insertNew += "VALUES (?,?,?,?,?,?,?,?)";
			insert = conn.prepareStatement(insertNew);
			insert.setString(1, item.folder);
			insert.setLong(2, item.enteredOn);
			insert.setString(3, item.requestType);
			insert.setString(4, item.requestedBy);
			insert.setLong(5, item.receivedOn);
			insert.setString(6,  item.receivedBy);
			insert.setString(7, item.status);
			insert.setString(8, item.remarks);
			
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
				update.setObject(1, log.getNewValue());
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

	public static int queryRowsAssignment(Connection conn, String filterStatus, String filterType, String filterFrom) throws SQLException
	{
		String where = buildWhere("Status", filterStatus, "RequestType", filterType, "RequestedBy", filterFrom);
		
		return queryRows(conn, where);
	}

	public static List<Monitoring> getRowsAssignment(Connection conn, String filterStatus, String filterType, String filterFrom, int rowStart, int rowEnd) throws SQLException
	{
		String where = buildWhere("Status", filterStatus, "RequestType", filterType, "RequestedBy", filterFrom);
		
		return getRows(conn, where, rowStart, rowEnd);
	}

	public static int queryRowsProcessing(Connection conn, String filterStatus, String filterType, String filterAssignedTo) throws SQLException
	{
		String where = buildWhere("Status", filterStatus, "RequestType", filterType, "AssignedTo", filterAssignedTo);
		
		return queryRows(conn, where);
	}

	public static List<Monitoring> getRowsProcessing(Connection conn, String filterStatus, String filterType, String filterAssignedTo, int rowStart, int rowEnd) throws SQLException
	{
		String where = buildWhere("Status", filterStatus, "RequestType", filterType, "AssignedTo", filterAssignedTo);
		
		return getRows(conn, where, rowStart, rowEnd);
	}

	public static String[] getFilterItems(Connection conn, String column) throws SQLException
	{
		column = Common.cleanColumnName(column);
		Statement select = conn.createStatement();
		ResultSet result = select.executeQuery(
				String.format("SELECT DISTINCT `%s` FROM `%s` ORDER BY `%s` ASC", 
						column, 
						TABLE_NAME,
						column));
		
		List<String> items = new ArrayList<String>();
		
		while (result.next())
		{
			try
			{
				String item = "";
				Object data = result.getObject(1);
				if (data != null) item = data.toString();
				if (!Utilities.isNullOrBlank(item)) items.add(item);
			}
			catch (Exception e)
			{
				// Silently ignore 
			}
		}
		
		String[] retobj = new String[items.size()];
		return items.toArray(retobj);
	}

	public static boolean createOutputMonitoringRaw(Connection conn, Filter filter, PrintWriter pw) throws SQLException
	{
		boolean success = false;
		String header = TABLE_COLS.replace("`", "\"");
		String where = "";
		String filterBuild = filter.build();
		if (!Utilities.isNullOrBlank(filterBuild)) where = "WHERE " + filterBuild;
		
		List<Monitoring> items = getRows(conn, where, "", "");
		if (items.size() > 0)
		{
			pw.println(header);
			
			for (Monitoring item : items)
			{
				write(item, pw);
			}
			
			success = true;
		}
		
		return success;
	}

	private static void write(Monitoring item, PrintWriter pw)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("%d", item.getId())); 
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getFolder())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvDate(item.getEnteredOn())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getRequestType())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getRequestedBy())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvDate(item.getReceivedOn())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getReceivedBy())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getStatus())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getAssignedBy())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getAssignedTo())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvDate(item.getAssignedOn())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getProcessedBy())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvDate(item.getProcessedOn())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvDate(item.getResolvedOn())));
		sb.append(",");
		sb.append(String.format("%s", Common.morphToCsvString(item.getRemarks())));
		
		pw.println(sb.toString());
	}
	
	
}
