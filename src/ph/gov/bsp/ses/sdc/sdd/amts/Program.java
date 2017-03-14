package ph.gov.bsp.ses.sdc.sdd.amts;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.*;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Version;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.MainWindow;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.ReceivingDialog;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.ReceivingTabComposite;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBox;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxButtons;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxIcon;

public class Program
{
//	static String USER_DOMAIN;
//	static String USER_NAME;
	private static String USER = String.format("%s\\%s", System.getenv("USERDOMAIN"), System.getenv("USERNAME"));
	
	static Configuration configuration = null;
	private static boolean receivingInitialized;
	
	static void testSQLITE()
	{
		Connection connection = null;
		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			
			statement.executeUpdate("drop table if exists person");
			statement.executeUpdate("create table person (id integer, name string)");
			statement.executeUpdate("insert into person values(1, 'leo')");
			statement.executeUpdate("insert into person values(2, 'yui')");
			ResultSet rs = statement.executeQuery("select * from person");
			while (rs.next())
			{
				// read the result set
				System.out.println("name = " + rs.getString("name"));
				System.out.println("id = " + rs.getInt("id"));
			}
		}
		catch (SQLException e)
		{
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if (connection != null) connection.close();
			}
			catch (SQLException e)
			{
				// connection close failed.
				System.err.println(e);
			}
		}
	}
	
	static void testSystemGetEnv()
	{
		Map<String, String> env = System.getenv();
		for (String key : env.keySet())
		{
			String line = "%s := %s";
			line = String.format(line, key, env.get(key));
			System.out.println(line);
		}
	}
	
	public static String getUser()
	{
		return USER;
	}

//	public static void setUser(String user)
//	{
//		USER = user;
//	}
	
	public static void main(String[] args)
	{
		try
		{			
//			Program.USER_DOMAIN = System.getenv("USERDOMAIN");
//			Program.USER_NAME = System.getenv("USERNAME");
//			Program.setUser(String.format("%s\\%s", Program.USER_DOMAIN, Program.USER_NAME));
			
			// #region Read configuration file
			
			File file = Configuration.getFile();
			if (!file.exists())
			{
				configuration = new Configuration();
				try
				{
					Configuration.write(file, configuration);
				}
				catch (IOException ioe)
				{
					// write failed
					ioe.printStackTrace();
					if (!MsgBox.show("Unable to write configuration file. Continue?", "Error", MsgBoxButtons.YES_NO, MsgBoxIcon.WARNING).isYes())
					{
						throw new UserAbortException("Unable to write configuration file. User aborted program.");
					}
				}
			}
			else
			{
				try
				{
					configuration = Configuration.read(file);
				}
				catch (IOException ioe)
				{
					// read failed
					ioe.printStackTrace();
					if (!MsgBox.show("Unable to read configuration file. Continue?", "Error", MsgBoxButtons.YES_NO, MsgBoxIcon.WARNING).isYes())
					{
						throw new UserAbortException("Unable to read configuration file. User aborted program.");
					}
				}
				catch (ClassNotFoundException cnfe)
				{
					// read failed, try to rewrite
					cnfe.printStackTrace();
					configuration = new Configuration();
					try
					{
						Configuration.write(file, configuration);
					}
					catch (IOException ioe2)
					{
						// write failed
						ioe2.printStackTrace();
						if (!MsgBox.show("Unable to rewrite configuration file. Continue?", "Error", MsgBoxButtons.YES_NO, MsgBoxIcon.WARNING).isYes())
						{
							throw new UserAbortException("Unable to rewrite configuration file. User aborted program.");
						}
					}
				}
				
			}
			
			// #endregion
			
			final MainWindow mw = new MainWindow();
			
			// load settings
			mw.getTextSqliteDb().setText(configuration.SqliteDbFilePath);
			mw.getTextFileServer().setText(configuration.FileServerPath);
			mw.getTextLocalCopy().setText(configuration.LocalCopyPath);
			
			// load about page
			mw.setInfoUrl(Utilities.getAbsolutePath("ABOUT.html"));
			
			mw.getDisplay();
			Realm.runWithDefault(SWTObservables.getRealm(mw.getDisplay()), new Runnable()
			{
				@Override
				public void run()
				{
					mw.open();
				}
			});
			
		}
		catch (UserAbortException uae)
		{
			uae.printStackTrace();
			System.exit(ExitCode.USER_ABORTED);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			System.exit(ExitCode.UNHANDLED_ERROR);
		}
	}
	
	public static void refreshReceivingTab(ReceivingTabComposite view, boolean forced)
	{
		if (receivingInitialized && !forced) return;
		
		Connection conn = null;
		
		try
		{
			// get filter criteria
			String filterType = view.getFilterType();
			String filterFrom = view.getFilterFrom();
			
			// get total row size from filter
			String connString = configuration.getSqliteConnectionString();
			conn = DriverManager.getConnection(connString);
			int rowTotal = Monitoring.queryRowsReceiving(conn, filterType, filterFrom);
			
			// get pagination
			int rowStart = view.getRowStart();
			int rowEnd = view.getRowEnd();
			
			// #region clean pagination data 
			
			// switch if unordered
			if (rowStart > rowEnd) 
			{
				int temp = rowEnd;
				rowEnd = rowStart;
				rowStart = temp;
			}
			
			int rowDiff = rowEnd - rowStart;
			
			if (rowStart > rowTotal) 
			{
				rowStart = 1;
				rowEnd = rowStart + rowDiff;
			}
			
			if (rowEnd > rowTotal) rowEnd = rowTotal;
			
			// #endregion
			
			// access database and get rows using search criteria
			List<Monitoring> rows = Monitoring.getRowsReceiving(conn, filterType, filterFrom, rowStart, rowEnd);
			
			// remove original children
			view.clear();
			
			if (rowTotal == 0) view.displayEmpty();
			else view.display(rows, rowStart, rowEnd, rowTotal);
			
			receivingInitialized = true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			MsgBox.show("Database connection failed.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
		finally
		{
			if (conn != null) 
				try
				{
					conn.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
		}
	}
	
	// #region UI - Settings
	
	/**
	 * <p>WARNING: This method spawns a UI element.</p>
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void browseSqliteDb(final MainWindow main, SelectionEvent buttonSelectedEvent)
	{
		FileDialog fd = new FileDialog(main.getShell(), SWT.OPEN);
		fd.setText("Select SQLite database file");
		// fd.setFilterPath("C:/");
		String[] filterExt = { "*.sqlite3", "*.*" };
		fd.setFilterExtensions(filterExt);
		final String selectedFile = fd.open();
		if (selectedFile != null)
		{
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextSqliteDb().setText(selectedFile);
				}
			});
		}
	}
	
	/**
	 * <p>WARNING: This method spawns a UI element.</p>
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void browseFileServer(final MainWindow main, SelectionEvent e)
	{
		DirectoryDialog dd = new DirectoryDialog(main.getShell(), SWT.OPEN);
		dd.setText("Select File Server");
		final String selectedDirectory = dd.open();
		if (selectedDirectory != null)
		{
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextFileServer().setText(selectedDirectory);
				}
			});
		}
	}
	
	/**
	 * <p>WARNING: This method spawns a UI element.</p>
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void browseLocalCopy(final MainWindow main, SelectionEvent e)
	{
		DirectoryDialog dd = new DirectoryDialog(main.getShell(), SWT.OPEN);
		dd.setText("Select Local Copy folder");
		final String selectedDirectory = dd.open();
		if (selectedDirectory != null)
		{
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextLocalCopy().setText(selectedDirectory);
				}
			});
		}
	}
	
	/**
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void checkSqliteDb(final MainWindow main, ModifyEvent textModifiedEvent)
	{
		main.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				String setting = main.getTextSqliteDb().getText();
				if (setting.equals(configuration.SqliteDbFilePath))
				{
					if (main.getTextSqliteDb().getBackground().equals(main.getColor_BG_VALID_SETTING()))
					{
						// it was just changed, no operation
					}
					else
					{
						main.getTextSqliteDb().setBackground(main.getDefaultColor("txtSqliteDb.background"));
					}
				}
				else
				{
					main.getTextSqliteDb().setBackground(main.getColor_BG_CHANGED_SETTING());
				}
			}
		});
	}
	
	/**
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void checkFileServer(final MainWindow main, ModifyEvent textModifiedEvent)
	{
		main.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				String setting = main.getTextFileServer().getText();
				if (setting.equals(configuration.FileServerPath))
				{
					if (main.getTextFileServer().getBackground().equals(main.getColor_BG_VALID_SETTING()))
					{
						// it was just changed, no operation
					}
					else
					{
						main.getTextFileServer().setBackground(main.getDefaultColor("txtFileServer.background"));
					}
				}
				else
				{
					main.getTextFileServer().setBackground(main.getColor_BG_CHANGED_SETTING());
				}
			}
		});
	}
	
	/**
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void checkLocalCopy(final MainWindow main, ModifyEvent textModifiedEvent)
	{
		main.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				String setting = main.getTextLocalCopy().getText();
				if (setting.equals(configuration.LocalCopyPath))
				{
					if (main.getTextLocalCopy().getBackground().equals(main.getColor_BG_VALID_SETTING()))
					{
						// it was just changed, no operation
					}
					else
					{
						main.getTextLocalCopy().setBackground(main.getDefaultColor("txtLocalCopy.background"));
					}
				}
				else
				{
					main.getTextLocalCopy().setBackground(main.getColor_BG_CHANGED_SETTING());
				}
			}
		});
	}

	/**
	 * <p>WARNING: This method spawns a UI element.</p>
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void setSqliteDb(final MainWindow main, SelectionEvent buttonSelectedEvent)
	{
		// cleanup input
		String filepath = main.getTextSqliteDb().getText().trim();
		if (filepath.startsWith("\"") && filepath.endsWith("\"")) filepath = filepath.substring(1, filepath.length() - 2);
		final String setting = filepath;
		
		// return the cleaned path to the text box
		main.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				main.getTextSqliteDb().setText(setting);
			}
		});
		
		File file = new File(setting);
		if (file.exists() && file.isFile())
		{
			String title = main.getTitle();
			main.setTitle(String.format("%s", "Checking SQLite database..."));
			
			boolean valid = true;
			
			Connection conn = null;
			try
			{
				try
				{
					String connString = Configuration.getSqliteConnectionString(setting);
					conn = DriverManager.getConnection(connString);
					
					// validateConnection() must catch its exceptions, but print
					// their stack traces for debugging
					// var = true & check > if a table fails, the entire var is
					// false
					valid = valid && new Version(conn).validateConnection();
					// TODO add more tables here
					
				}
				finally
				{
					if (conn != null) conn.close();
				}
			}
			catch (SQLException e)
			{
				// weird if this happens, set valid to false, debug it later
				e.printStackTrace();
				valid = false;
			}
			
			if (valid)
			{
				// Set background to show it's valid
				main.getDisplay().asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						main.getTextSqliteDb().setBackground(main.getColor_BG_VALID_SETTING());
					}
				});
				
				try
				{
					configuration.SqliteDbFilePath = setting;
					Configuration.write(Configuration.getFile(), configuration);
				}
				catch (IOException e)
				{
					// failed to write configuration
					e.printStackTrace();
					MsgBox.show(main.getShell(), "An error occurred while writing the configuration file. Your new setting may only be in effect until the program closes.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
				}
			}
			else
			{
				// Set background to show it's invalid
				main.getDisplay().asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						main.getTextSqliteDb().setBackground(main.getColor_BG_INVALID_SETTING());
					}
				});
				MsgBox.show(main.getShell(), String.format("The file %s is invalid.", setting), "Invalid file", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
			}
			
			main.setTitle(title);
		}
		else
		{ 
			// Set background to show it's invalid
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextSqliteDb().setBackground(main.getColor_BG_INVALID_SETTING());
				}
			});
			MsgBox.show(main.getShell(), String.format("The path %s is not an existing file.", setting), "Invalid path", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}
	
	/**
	 * <p>WARNING: This method spawns a UI element.</p>
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void setFileServer(final MainWindow main, SelectionEvent buttonSelectedEvent)
	{
		// cleanup input
		String filepath = main.getTextFileServer().getText().trim();
		if (filepath.startsWith("\"") && filepath.endsWith("\"")) filepath = filepath.substring(1, filepath.length() - 2);
		final String setting = filepath;
		
		// return the cleaned path to the text box
		main.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				main.getTextFileServer().setText(setting);
			}
		});
		
		File file = new File(setting);
		if (file.exists() && file.isDirectory())
		{
			// No further checks needed
			
			// Set background to show it's valid
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextFileServer().setBackground(main.getColor_BG_VALID_SETTING());
				}
			});
			
			try
			{
				configuration.FileServerPath = setting;
				Configuration.write(Configuration.getFile(), configuration);
			}
			catch (IOException e)
			{
				// failed to write configuration
				e.printStackTrace();
				MsgBox.show(main.getShell(), "An error occurred while writing the configuration file. Your new setting may only be in effect until the program closes.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
			}
		}
		else
		{ 
			// Set background to show it's invalid
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextFileServer().setBackground(main.getColor_BG_INVALID_SETTING());
				}
			});
			MsgBox.show(main.getShell(), String.format("The path %s is not an existing directory.", setting), "Invalid path", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}
	
	/**
	 * <p>WARNING: This method spawns a UI element.</p>
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public static void setLocalCopy(final MainWindow main, SelectionEvent buttonSelectedEvent) 
	{
		// cleanup input
		String filepath = main.getTextLocalCopy().getText().trim();
		if (filepath.startsWith("\"") && filepath.endsWith("\"")) filepath = filepath.substring(1, filepath.length() - 2);
		final String setting = filepath;
		
		// return the cleaned path to the text box
		main.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				main.getTextLocalCopy().setText(setting);
			}
		});
		
		File file = new File(setting);
		if (file.exists() && file.isDirectory())
		{
			// No further checks needed
			
			// Set background to show it's valid
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextLocalCopy().setBackground(main.getColor_BG_VALID_SETTING());
				}
			});
			
			try
			{
				configuration.LocalCopyPath = setting;
				Configuration.write(Configuration.getFile(), configuration);
			}
			catch (IOException e)
			{
				// failed to write configuration
				e.printStackTrace();
				MsgBox.show(main.getShell(), "An error occurred while writing the configuration file. Your new setting may only be in effect until the program closes.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
			}
		}
		else
		{
			// Set background to show it's invalid
			main.getDisplay().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextLocalCopy().setBackground(main.getColor_BG_INVALID_SETTING());
				}
			});
			MsgBox.show(main.getShell(), String.format("The path %s is not an existing directory.", setting), "Invalid path", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}

	// #endregion
	
	/**
	 * <p>WARNING: This method spawns a UI element.</p>
	 */
	public static void receive(Shell mainShell, SelectionEvent buttonSelectedEvent)
	{
		FileDialog fd = new FileDialog(mainShell, SWT.OPEN | SWT.MULTI);
		fd.setFilterExtensions(Utilities.toArray("*.*"));
		fd.setText("Select reference files");
		String output = fd.open();
		
		if (output == null) return;
		
		String[] files = fd.getFileNames();
		String base = fd.getFilterPath();
		
		// try to create at file server
		boolean dirMade = false;
		File dirFileServer = new File(configuration.FileServerPath);
		File targetBase = null;
		long receivedOn = 0;
		String folder = null;
		for (int i = 1; i <= configuration.mkdirTries; i++)
		{
			receivedOn = System.currentTimeMillis();
			folder = "" + receivedOn;
			targetBase = new File(dirFileServer, folder);
			if (dirMade = targetBase.mkdirs()) break;
		}
		
		if (!dirMade)
		{
			MsgBox.show(mainShell, String.format("Unable to create folder in %s after %d tries.", dirFileServer, configuration.mkdirTries), "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
			return;
		}	
		
		File sourceBase = new File(base);
		for (int i = 0; i < files.length; i++)
		{
			File sourceFile = new File(sourceBase, files[i]);
			File targetFile = new File(targetBase, files[i]);
			try
			{
				FileUtils.copyFile(sourceFile, targetFile);
			}
			catch (IOException e)
			{
				MsgBox.show(mainShell, String.format("Unable to copy file \"%s\" to \"%s\".", sourceFile.getAbsolutePath(), targetFile.getAbsolutePath()), "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
				e.printStackTrace();
				return;
			}
		}
		
		// reaching here means the copy is successful
		
		Monitoring item = new Monitoring();
		item.setFolder(folder);
		item.setReceivedOn(new Date(receivedOn));
		item.setReceivedBy(Program.USER);
		item.setApprovalStatus("UNAPPROVED");
		
		ReceivingDialog edit = new ReceivingDialog(mainShell, SWT.NONE);
		edit.setItem(item);
		if (edit.open())
		{
			Utilities.dump(item, System.out);
		}
		else
		{
			System.out.println("CANCELLED");
		}
	}

	/**
	 * <p>WARNING: This method spawns a UI element.</p> 
	 */
	public static boolean newEntry(Shell shell, Monitoring item)
	{
		boolean success = false;
		Connection conn = null;
		
		try
		{
			try
			{
				String connString = configuration.getSqliteConnectionString();
				conn = DriverManager.getConnection(connString);
				
				Monitoring.newEntry(conn, item);
				
				success = true;
			}
			finally
			{
				if (conn != null)
				{
					conn.close();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(shell, "Unable to create new entry.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
		
		return success;
	}
}
