package ph.gov.bsp.ses.sdc.sdd.amts.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Version
{
	
	private static final String TABLE_NAME = "VERSION";
	private static final String TABLE_FIELDS = "`MAJOR`,`MINOR`";
	
	private Connection conn;
	
	public Version(Connection conn)
	{
		this.conn = conn;
	}
	
	protected String get() throws SQLException
	{
		String retval = null;
		Statement select = conn.createStatement();
		ResultSet result = select.executeQuery(String.format("SELECT %s FROM %s", TABLE_FIELDS, TABLE_NAME));
		while (result.next())
		{
			int major = getMajor(result);
			int minor = getMinor(result);
			retval = String.format("%d.%d", major, minor);
			break;
		}
		return retval;
	}
	
	private int getMajor(ResultSet result) throws SQLException
	{
		return result.getInt("MAJOR");
	}
	
	private int getMinor(ResultSet result) throws SQLException
	{
		return result.getInt("MINOR");
	}
	
	protected static boolean check(String version)
	{
		version = version.toUpperCase();
		boolean valid = false;
		valid |= version.equals("1.0");
		// add more versions here
		return valid;
	}
	
	public boolean validateConnection()
	{
		try
		{
			return check(get());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
}
