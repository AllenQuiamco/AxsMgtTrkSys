package ph.gov.bsp.ses.sdc.sdd.amts.data;

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
import java.util.Random;

public class Monitoring implements Cloneable
{
	private static final String TABLE_NAME = "Monitoring";
	private static final String TABLE_COLS = "`ID`,`Folder`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`ApprovalStatus`,`AssignedBy`,`AssignedTo`,`AssignedOn`,`ProcessDetails`,`ProcessedBy`,`ProcessedOn`,`ResolvedOn`,`Remarks`";
	
	private int id;
	private String folder;
	private String requestType;
	private String requestedBy;
	private Date receivedOn = new Date(System.currentTimeMillis());
	private String receivedBy;
	private String approvalStatus;
	private String assignedBy;
	private String assignedTo;
	private Date assignedOn;
	private String processDetails;
	private String processedBy;
	private Date processedOn;
	private Date resolvedOn;
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
		return receivedOn;
	}
	
	public void setReceivedOn(Date receivedOn)
	{
		this.receivedOn = receivedOn;
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
		return assignedOn;
	}

	public void setAssignedOn(Date assignedOn)
	{
		this.assignedOn = assignedOn;
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
		return processedOn;
	}

	public void setProcessedOn(Date processedOn)
	{
		this.processedOn = processedOn;
	}

	public Date getResolvedOn()
	{
		return resolvedOn;
	}

	public void setResolvedOn(Date resolvedOn)
	{
		this.resolvedOn = resolvedOn;
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
	public static LinkedList<Monitoring> getSamples()
	{
		LinkedList<Monitoring> retobj = new LinkedList<Monitoring>();
		
		Random random = new Random();
		Monitoring item; 
		int max = new Random().nextInt(10) + 1;
		
		for(int i = 0; i < max; i++)
		{
			item = new Monitoring();
			item.id = random.nextInt(100);
			item.folder = String.format("folder%08x", random.nextInt());
			item.requestType = String.format("requestType%08x", random.nextInt());
			item.requestedBy = String.format("requestedBy%08x", random.nextInt());
			item.receivedOn = new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1);
			item.receivedBy = String.format("receivedBy%08x", random.nextInt());
			item.approvalStatus = String.format("approvalStatus%08x", random.nextInt());
			item.assignedBy = String.format("assignedBy%08x", random.nextInt());
			item.assignedTo = String.format("assignedTo%08x", random.nextInt());
			item.assignedOn = new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1);
			item.processDetails = String.format("processDetails%08x", random.nextInt());
			item.processedBy = String.format("processedBy%08x", random.nextInt());
			item.processedOn = new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1);
			item.resolvedOn = new Date((117 - 20) + random.nextInt(20), random.nextInt(12), random.nextInt(28) + 1);
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
					e.printStackTrace();
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
	 * WARNING: This method disables auto-commit of the argument {@link Connection}.
	 */
	public static void newEntry(Connection conn, Monitoring item) throws Exception
	{
		conn.setAutoCommit(false);
		
		try
		{
			String insertNew = String.format("INSERT INTO %s (`Folder`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`ApprovalStatus`,`Remarks`) VALUES (?,?,?,?,?,?,?)", 
					TABLE_NAME);
			PreparedStatement insert = conn.prepareStatement(insertNew);
			
			// TODO insert new item
			
			conn.commit();
		}
		catch (Exception e)
		{
			conn.rollback();
			throw e;
		}
	}
	
}
