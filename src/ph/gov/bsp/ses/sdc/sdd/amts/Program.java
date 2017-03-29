package ph.gov.bsp.ses.sdc.sdd.amts;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.sqlite.SQLiteConfig;

import ph.gov.bsp.ses.sdc.sdd.amts.data.Filter;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Log;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Version;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.AssignmentComposite;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.AssignmentDialog;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.MainWindow;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.PaginationComposite;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.ProcessingComposite;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.ProcessingDialog;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.ReceivingDialog;
import ph.gov.bsp.ses.sdc.sdd.amts.ui.ReceivingComposite;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBox;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxButtons;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxIcon;

public class Program
{
	private static final String VERSION = "v0.2.17088.0";
	private static final String APP_NAME = "amts";
	private static final String SETTINGS_FILE_NAME = "settings.ini";
	public static final String USER = String.format("%s\\%s", System.getenv("USERDOMAIN"), System.getenv("USERNAME"));
	
	private static Settings settings = null;
	private static boolean receivingInitialized;
	private static boolean assignmentInitialized;
	private static boolean processingInitialized;
	
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
	
	static void testLoadSamples() throws Exception
	{
		Connection conn = null;
		
		try
		{
			conn = getConnection();
			conn.setAutoCommit(false);
			
			List<Monitoring> samples = Monitoring.getSamples(50);
			for (Monitoring sample : samples)
			{
				Monitoring.addNew(conn, sample);
			}
			
			conn.commit();
		}
		finally
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		
		System.exit(ExitCode.HARD_ABORT);
	}
	
	private static File getSettingsFile()
	{
		String filedir = System.getenv("APPDATA");
		if (!new File(filedir).exists()) filedir = System.getenv("USERPROFILE");
		else if (!new File(filedir).exists()) filedir = System.getenv("USER_HOME");
		else if (!new File(filedir).exists()) filedir = "";
		
		if (!filedir.equals(""))
		{ // load from user directory
			File file = new File(filedir, APP_NAME);
			if (!file.exists()) file.mkdir();
			filedir = file.getAbsolutePath();
		}
		
		File file = new File(filedir, SETTINGS_FILE_NAME);
		return file;
	}
	
	private static Settings getSettings()
	{
		return (settings == null) ? (settings = Settings.get(getSettingsFile())) : settings;
	}
	
	public static String getSetting(String setting)
	{
		return getSettings().get(setting);
	}
	
	public static void setSetting(String setting, String value)
	{
		getSettings().set(setting, value);
	}
	
	public static Connection getConnection() throws SQLException
	{
		return getConnection(getSetting("path.sqlitedb"));
	}
	
	public static Connection getConnection(String sqliteFilePath) throws SQLException
	{
		String connString = String.format("jdbc:sqlite:%s", sqliteFilePath.replace("\\", "/"));
		
		SQLiteConfig config = new SQLiteConfig();
		config.setBusyTimeout(getSetting("data.busytimeoutmsecs"));
		
		return DriverManager.getConnection(connString, config.toProperties());
	}
	
	public static void main(String[] args)
	{
		try
		{	
//			Date date = new Date(System.currentTimeMillis());
//			System.out.println(Monitoring.morphDate(date));
//			testLoadSamples();
//			System.exit(ExitCode.HARD_ABORT);
						
			final MainWindow mw = new MainWindow();
			mw.setNoSettings(!(getSettingsFile().exists()));
			
			// load settings
			mw.getTextSqliteDb().setText(getSetting("path.sqlitedb"));
			mw.getTextFileServer().setText(getSetting("path.fileserver"));
			mw.getTextLocalCopy().setText(getSetting("path.localcopy"));
			mw.setTitleVersion(VERSION);
			
			// load about page
			mw.setInfoUrl(Utilities.getAbsolutePath("ABOUT.html"));
			
			Realm.runWithDefault(SWTObservables.getRealm(mw.getDisplay()), new Runnable()
			{
				@Override
				public void run()
				{
					mw.open();
				}
			});	
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			System.exit(ExitCode.UNHANDLED_ERROR);
		}
	}
	
	// #region UI - Settings
	
	/**
	 * <p>WARNING: This method spawns a UI element.</p>
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
			main.getDisplay().syncExec(new Runnable()
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
	 */
	public static void browseFileServer(final MainWindow main, SelectionEvent e)
	{
		DirectoryDialog dd = new DirectoryDialog(main.getShell(), SWT.OPEN);
		dd.setText("Select File Server");
		final String selectedDirectory = dd.open();
		if (selectedDirectory != null)
		{
			main.getDisplay().syncExec(new Runnable()
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
	 */
	public static void browseLocalCopy(final MainWindow main, SelectionEvent e)
	{
		DirectoryDialog dd = new DirectoryDialog(main.getShell(), SWT.OPEN);
		dd.setText("Select Local Copy folder");
		final String selectedDirectory = dd.open();
		if (selectedDirectory != null)
		{
			main.getDisplay().syncExec(new Runnable()
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
				if (setting.equals(getSetting("path.sqlitedb")))
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
				if (setting.equals(getSetting("path.fileserver")))
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
				if (setting.equals(getSetting("path.localcopy")))
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
	 */
	public static void setSqliteDb(final MainWindow main, SelectionEvent buttonSelectedEvent)
	{
		// cleanup input
		String filepath = main.getTextSqliteDb().getText().trim();
		if (filepath.startsWith("\"") && filepath.endsWith("\"")) filepath = filepath.substring(1, filepath.length() - 2);
		final String setting = filepath;
		
		// return the cleaned path to the text box
		main.getDisplay().syncExec(new Runnable()
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
					conn = getConnection(setting);
					
					// validateConnection() must catch its exceptions, but print
					// their stack traces for debugging
					// var = true & check > if a table fails, the entire var is
					// false
					valid = valid && new Version(conn).validateConnection();
					// TODO Add more tables here
					
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
				main.getDisplay().syncExec(new Runnable()
				{
					@Override
					public void run()
					{
						main.getTextSqliteDb().setBackground(main.getColor_BG_VALID_SETTING());
					}
				});
				
				setSetting("path.sqlitedb", setting);
			}
			else
			{
				// Set background to show it's invalid
				main.getDisplay().syncExec(new Runnable()
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
			main.getDisplay().syncExec(new Runnable()
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
	 */
	public static void setFileServer(final MainWindow main, SelectionEvent buttonSelectedEvent)
	{
		// cleanup input
		String filepath = main.getTextFileServer().getText().trim();
		if (filepath.startsWith("\"") && filepath.endsWith("\"")) filepath = filepath.substring(1, filepath.length() - 2);
		final String setting = filepath;
		
		// return the cleaned path to the text box
		main.getDisplay().syncExec(new Runnable()
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
			main.getDisplay().syncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextFileServer().setBackground(main.getColor_BG_VALID_SETTING());
				}
			});
			
			setSetting("path.fileserver", setting);
		}
		else
		{ 
			// Set background to show it's invalid
			main.getDisplay().syncExec(new Runnable()
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
		main.getDisplay().syncExec(new Runnable()
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
			main.getDisplay().syncExec(new Runnable()
			{
				@Override
				public void run()
				{
					main.getTextLocalCopy().setBackground(main.getColor_BG_VALID_SETTING());
				}
			});
			
			setSetting("path.localcopy", setting);
		}
		else
		{
			// Set background to show it's invalid
			main.getDisplay().syncExec(new Runnable()
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
	public static void receive(Shell shell, SelectionEvent buttonSelectedEvent)
	{
		try
		{
			FileDialog fd = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
			fd.setFilterExtensions(Utilities.toArray("*.*"));
			fd.setText("Select reference files");
			String output = fd.open();
			
			if (output == null) return;
			
			String[] files = fd.getFileNames();
			String base = fd.getFilterPath();
			
//			StringBuilder sb = new StringBuilder();
//			for (String file : files)
//			{
//				if (sb.length() > 0) sb.append(String.format("%n"));
//				sb.append(new File(file).getName());
//			}
//			MsgBoxResult r = MsgBox.show(shell, String.format("A new entry will be created for the following file%s:%n%n%s%n%nAre you sure you want to continue?", (files.length > 1) ? "s" : "", sb.toString()), "Confirm new entry", MsgBoxButtons.YES_NO, MsgBoxIcon.QUESTION);
//			if (!r.isYes()) return;
			
			// try to create at file server
			boolean dirMade = false;
			File dirFileServer = new File(getSetting("path.fileserver"));
			File targetBase = null;
			long enteredOn = 0;
			String folder = null;
			int tries = Integer.parseInt(getSetting("data.mkdirtries"));
			for (int i = 1; i <= tries; i++)
			{
				enteredOn = System.currentTimeMillis();
				folder = "" + enteredOn;
				targetBase = new File(dirFileServer, folder);
				if (dirMade = targetBase.mkdirs()) break;
			}
			
			if (!dirMade)
			{
				throw new IOException(
						String.format(
								"Unable to create folder in %s after %s tries.", 
								dirFileServer, 
								getSetting("path.mkdirtries")));
			}	
			
			boolean error = false;
			
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
					error = true;
					e.printStackTrace();
					MsgBox.show(shell, 
							String.format("Unable to copy file \"%s\" to \"%s\".", 
									sourceFile.getAbsolutePath(), 
									targetFile.getAbsolutePath()), 
									"Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
				}
			}
			
			if (!error)
			{
				Monitoring item = new Monitoring();
				item.setFolder(folder);
				item.setEnteredOn(new Date(enteredOn));
				item.setReceivedBy(Program.USER);
				item.setStatus("NEW");
				
				ReceivingDialog edit = new ReceivingDialog(shell, SWT.NONE);
				edit.setItem(item);
				
				if (edit.open())
				{
					Monitoring edited = edit.getEditedItem();
					error = !newEntry(shell, edited);
				} 
				else error = true;
			}
			
			if (error)
			{
				// Delete folder
				FileUtils.deleteQuietly(targetBase);
			}
		}
		catch (IOException e)
		{ 
			e.printStackTrace();
			MsgBox.show(shell, "Unable to create new entry.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
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
				conn = getConnection();
				conn.setAutoCommit(false);
				
				Monitoring.addNew(conn, item);
				
				int rowId = Monitoring.getIdFromFolder(conn, item.getFolder());
				
				Log logItem = new Log();
				logItem.setUserId(Program.USER);
				logItem.setAction("new entry");
				logItem.setTableName("MONITORING");
				logItem.setRowId(rowId);
				
				Log.append(conn, logItem, Program.USER, new Date(System.currentTimeMillis()));
				
				conn.commit();
				
				success = true;
			}
			catch (Exception e)
			{
				success = false;
				
				conn.rollback();
				throw e; // rethrow
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
	
	public static void refreshReceivingTab(ReceivingComposite view, boolean forced)
	{
		if (receivingInitialized && !forced) return;
		
		Connection conn = null;
		
		try
		{
			// get filter criteria
			String filterType = view.getFilterType();
			String filterFrom = view.getFilterFrom();
			
			// get total row size from filter
			conn = getConnection();
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

	public static void updateReceiving(Shell shell, TableItem tableItem, int id)
	{
		try
		{
			Connection conn = null;
			Monitoring item = null;
			
			try
			{
				conn = getConnection();
				
				item = Monitoring.getItem(conn, id);
			}
			finally
			{
				if (conn != null) conn.close();
			}
			
			if (item != null)
			{
				ReceivingDialog edit = new ReceivingDialog(shell, SWT.NONE);
				
				edit.setItem(item);
				if (edit.open())
				{
					List<Log> logs = edit.getChangeLog();
					if (logs.size() <= 0) return;
					
					try
					{
						conn = getConnection();						
						conn.setAutoCommit(false);
						
						Monitoring.update(conn, logs);
						Log.append(conn, logs, Program.USER, new Date(System.currentTimeMillis()));
						
						conn.commit();
						
						Monitoring row = Monitoring.getItem(conn, id);
						ReceivingComposite.setText(tableItem, row);
					}
					catch (Exception e)
					{
						conn.rollback();
						throw e;
					}
					finally
					{
						if (conn != null) conn.close(); 
					}
				}
			}
			else
			{
				throw new NullPointerException("Monitoring.getItem(Connection, int) returned null");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(shell, "Unable to update.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}

	public static void refreshAssignmentTab(AssignmentComposite view, boolean forced)
	{
		if (assignmentInitialized && !forced) return;
		
		Connection conn = null;
		
		try
		{
			// get filter criteria
			String filterStatus = view.getFilterStatus();
			String filterType = view.getFilterType();
			String filterFrom = view.getFilterFrom();
			
			// get total row size from filter
			conn = getConnection();
			int rowTotal = Monitoring.queryRowsAssignment(conn, filterStatus, filterType, filterFrom);
			
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
			if (rowDiff < PaginationComposite.DEFAULT_ROW_DELTA) rowEnd = rowStart + PaginationComposite.DEFAULT_ROW_DELTA; 
			
			if (rowStart > rowTotal) 
			{
				rowStart = 1;
				rowEnd = rowStart + rowDiff;
			}
			
			if (rowEnd > rowTotal) rowEnd = rowTotal;
			
			// #endregion
			
			// access database and get rows using search criteria
			List<Monitoring> rows = Monitoring.getRowsAssignment(conn, filterStatus, filterType, filterFrom, rowStart, rowEnd);
			
			// remove original children
			view.clear();
			
			if (rowTotal == 0) view.displayEmpty();
			else view.display(rows, rowStart, rowEnd, rowTotal);
			
			assignmentInitialized = true;
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

	public static void updateAssignment(Shell shell, TableItem tableItem, int id)
	{
		try
		{
			Connection conn = null;
			Monitoring item = null;
			
			try
			{
				conn = getConnection();
				
				item = Monitoring.getItem(conn, id);
			}
			finally
			{
				if (conn != null) conn.close();
			}
			
			if (item != null)
			{
				AssignmentDialog edit = new AssignmentDialog(shell, SWT.NONE);
				
				edit.setItem(item);
				if (edit.open())
				{
					List<Log> logs = edit.getChangeLog();
					if (logs.size() <= 0) return;
					
					try
					{
						conn = getConnection();
						conn.setAutoCommit(false);
						
						Monitoring.update(conn, logs);
						Log.append(conn, logs, Program.USER, new Date(System.currentTimeMillis()));
						
						conn.commit();
						
						Monitoring row = Monitoring.getItem(conn, id);
						AssignmentComposite.setText(tableItem, row);
					}
					catch (Exception e)
					{
						conn.rollback();
						throw e;
					}
					finally
					{
						if (conn != null) conn.close(); 
					}
				}
			}
			else
			{
				throw new NullPointerException("Monitoring.getItem(Connection, int) returned null");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(shell, "Unable to update.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}

	public static void refreshProcessingTab(ProcessingComposite view, boolean forced)
	{
		if (processingInitialized && !forced) return;
		
		Connection conn = null;
		
		try
		{
			// get filter criteria
			String filterStatus = view.getFilterStatus();
			String filterType = view.getFilterType();
			String filterAssignedTo = view.getFilterAssignedTo();
			
			// get total row size from filter
			conn = getConnection();
			int rowTotal = Monitoring.queryRowsProcessing(conn, filterStatus, filterType, filterAssignedTo);
			
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
			List<Monitoring> rows = Monitoring.getRowsProcessing(conn, filterStatus, filterType, filterAssignedTo, rowStart, rowEnd);
			
			// remove original children
			view.clear();
			
			if (rowTotal == 0) view.displayEmpty();
			else view.display(rows, rowStart, rowEnd, rowTotal);
			
			processingInitialized = true;
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
	
	public static void updateProcessing(Shell shell, TableItem tableItem, int id)
	{
		try
		{
			Connection conn = null;
			Monitoring item = null;
			
			try
			{
				conn = getConnection();
				
				item = Monitoring.getItem(conn, id);
			}
			finally
			{
				if (conn != null) conn.close();
			}
			
			if (item != null)
			{
				ProcessingDialog edit = new ProcessingDialog(shell, SWT.NONE);
				
				edit.setItem(item);
				if (edit.open())
				{
					List<Log> logs = edit.getChangeLog();
					if (logs.size() <= 0) return;
					
					try
					{
						conn = getConnection();
						conn.setAutoCommit(false);
						
						Monitoring.update(conn, logs);
						Log.append(conn, logs, Program.USER, new Date(System.currentTimeMillis()));
						
						conn.commit();
						
						Monitoring row = Monitoring.getItem(conn, id);
						ProcessingComposite.setText(tableItem, row);
					}
					catch (Exception e)
					{
						conn.rollback();
						throw e;
					}
					finally
					{
						if (conn != null) conn.close(); 
					}
				}
			}
			else
			{
				throw new NullPointerException("Monitoring.getItem(Connection, int) returned null");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(shell, "Unable to update.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}

	public static void createLocalCopy(Shell shell, int id)
	{
		try
		{
			Connection conn = null;
			Monitoring item = null; 
			
			try
			{
				conn = getConnection();
				item = Monitoring.getItem(conn, id);
			}
			finally 
			{
				if (conn != null) conn.close();
			}
			
			if (item == null) throw new NullPointerException("Monitoring.getItem(Connection, int) returned null");
			
			String path;
			
			path = getSetting("path.fileserver");
			File srcbase = new File(path);
			if (!srcbase.exists()) throw new FileNotFoundException(String.format("The path \"%s\" does not exist.", path));
			if (!srcbase.isDirectory()) throw new FileNotFoundException(String.format("The path \"%s\" is not a directory.", path));
			
			path = getSetting("path.localcopy");
			File tgtbase = new File(path);
			if (!tgtbase.exists()) throw new FileNotFoundException(String.format("The path \"%s\" does not exist.", path));
			if (!tgtbase.isDirectory()) throw new FileNotFoundException(String.format("The path \"%s\" is not a directory", path));
			
			File src_dir = new File(srcbase, item.getFolder());
			if (!src_dir.exists()) throw new FileNotFoundException(String.format("Folder \"%s\" is missing.", item.getFolder()));
			if (!src_dir.isDirectory()) throw new FileNotFoundException(String.format("The path \"%s\" is not a directory", src_dir.getAbsolutePath()));
			
			File tgt_dir = new File(tgtbase, item.getFolder());
			if (tgt_dir.exists()) 
			{
				for (int i = 1; i <= Integer.MAX_VALUE; i++)
				{
					tgt_dir = new File(tgtbase, String.format("%s-%d", item.getFolder(), i));
					if (!tgt_dir.exists())
					{
						if (!tgt_dir.mkdir()) throw new IOException("File.mkdir() failed on path " + tgt_dir.getAbsolutePath());
						break;
					}
				}
			}
			else if (!tgt_dir.mkdir()) throw new IOException("File.mkdir() failed on path " + tgt_dir.getAbsolutePath());
			
			// Copy the files
			for (File src : src_dir.listFiles())
			{
				FileUtils.copyFile(src, new File(tgt_dir, src.getName()));
			}
			
			Desktop.getDesktop().open(tgt_dir);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			MsgBox.show(shell, e.getMessage(), "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(shell, "Unable to create local copy.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}

	public static String[] getMonitoringFilterItems(Shell shell, String string)
	{
		try
		{
			Connection conn = null;
			
			try
			{
				conn = getConnection();
				return Monitoring.getFilterItems(conn, string);
			}
			finally
			{
				if (conn != null) conn.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(shell, "Unable to get filter items.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
			return null;
		}	
	}

	public static void createOutputMonitoringRaw(Shell shell, Filter filter)
	{
		try
		{
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFilterExtensions(Utilities.toArray("*.csv", "*.*"));
			dialog.setFilterNames(Utilities.toArray("Comma-Separated Values", "All Files"));
			String target = dialog.open();
			if (Utilities.isNullOrBlank(target)) return;
			
			PrintWriter pw = new PrintWriter(target);
			Connection conn = null;
			boolean success = false;
			
			try
			{
				conn = getConnection();
				success = Monitoring.createOutputMonitoringRaw(conn, filter, pw);
			}
			finally
			{
				if (conn != null) conn.close();
				if (pw != null) 
				{
					pw.close();
					if (!success) FileUtils.deleteQuietly(new File(target));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(shell, "An unexpected error occurred.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}

}
