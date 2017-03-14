package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Monitoring
{
	private static final String TABLE_NAME = "Monitoring";
	private static final String TABLE_COLS = "`ID`,`Folder`,`RequestType`,`RequestedBy`,`ReceivedOn`,`ReceivedBy`,`ApprovalStatus`,`AssignedBy`,`AssignedTo`,`AssignedOn`,`ProcessDetails`,`ProcessedBy`,`ProcessedOn`,`ResolvedOn`,`Remarks`";
	
	public int ID;
	public String Folder;
	public String RequestType;
	public String RequestedBy;
	public Date ReceivedOn = new Date(System.currentTimeMillis());
	public String ReceivedBy;
	public String ApprovalStatus;
	public String AssignedBy;
	public String AssignedTo;
	public Date AssignedOn;
	public String ProcessDetails;
	public String ProcessedBy;
	public String ProcessedOn;
	public Date ResolvedOn;
	public String Remarks;
	
	public static LinkedList<Monitoring> getSamples()
	{
		LinkedList<Monitoring> retobj = new LinkedList<Monitoring>();
		
		Random random = new Random();
		Monitoring item; 
		int max = new Random().nextInt(10) + 1;
		
		for(int i = 0; i < max; i++)
		{
			item = new Monitoring();
			item.ID = random.nextInt(100);
			item.Folder = String.format("folder%08x", random.nextInt());
			
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
				collect &= (modifiers & Modifier.PUBLIC) != 0; // is public
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
	
}
