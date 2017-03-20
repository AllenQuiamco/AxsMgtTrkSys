package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.util.Hashtable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.RowLayout;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

/**
 * The main window of the application.
 * 
 * @author © 2017 Miguel Aguinaldo
 * 
 */
public class MainWindow
{
	
	/**
	 * An instance's reference to itself. Useful with anonymous inner types.
	 */
	private MainWindow self;
	
	private Shell shell;
	private Display display;
	private Text txtLoggedOnAs;
	private Browser cmpInfoBrowser;
	private Text txtSqliteDb;
	private Text txtFileServer;
	private Text txtLocalCopy;
	private Hashtable<String, Color> defaultColors = new Hashtable<String, Color>();

	private ReceivingComposite xcmpReceiving;
	private AssignmentComposite xcmpAssignment;
	
	public MainWindow()
	{
		display = Display.getDefault();
		createContents();
		self = this;
	}
	
	/**
	 * Open the window.
	 */
	public void open()
	{
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		shell = new Shell();
		shell.setMinimumSize(new Point(450, 300));
		shell.setImage(SWTResourceManager.getImage(MainWindow.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/java-16x16-32bit.png"));
		shell.setSize(450, 300);
		shell.setText("Access Management Tracking System");
		FillLayout fl_shell = new FillLayout(SWT.VERTICAL);
		fl_shell.spacing = 1;
		fl_shell.marginWidth = 2;
		fl_shell.marginHeight = 2;
		shell.setLayout(fl_shell);
		shell.addShellListener(new ShellAdapter()
		{
			@Override
			public void shellClosed(ShellEvent e)
			{
				//close(e);
				e.doit = true; // TODO Save window position information
			}
		});
		
		final TabFolder tabs = new TabFolder(shell, SWT.NONE);
		tabs.addSelectionListener(new SelectionAdapter()
		{	
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				switch (tabs.getSelectionIndex())
				{
					case 1:
						Program.refreshReceivingTab(xcmpReceiving, false);
					case 2:
						Program.refreshAssignmentTab(xcmpAssignment, false);
						break;
				}
			}
		});
		
		TabItem tabInfo = new TabItem(tabs, SWT.NONE);
		tabInfo.setText("Info");
		
		Composite cmpInfo = new Composite(tabs, SWT.NONE);
		tabInfo.setControl(cmpInfo);
		cmpInfo.setLayout(new FormLayout());
		
		Composite cmpInfoUser = new Composite(cmpInfo, SWT.NONE);
		cmpInfoUser.setLayout(new GridLayout(2, false));
		FormData fd_cmpInfoUser = new FormData();
		fd_cmpInfoUser.top = new FormAttachment(0);
		fd_cmpInfoUser.left = new FormAttachment(0);
		fd_cmpInfoUser.bottom = new FormAttachment(0, 31);
		fd_cmpInfoUser.right = new FormAttachment(0, 260);
		cmpInfoUser.setLayoutData(fd_cmpInfoUser);
		
		Label lblLoggedOnAs = new Label(cmpInfoUser, SWT.NONE);
		lblLoggedOnAs.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLoggedOnAs.setText("Logged on as:");
		
		txtLoggedOnAs = new Text(cmpInfoUser, SWT.BORDER);
		txtLoggedOnAs.setText(Program.USER);
		txtLoggedOnAs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Button btnInfoBrowserBack = new Button(cmpInfo, SWT.FLAT);
		btnInfoBrowserBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				display.asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						cmpInfoBrowser.back();
					}
				});
			}
		});
		btnInfoBrowserBack.setEnabled(false);
		btnInfoBrowserBack.setImage(SWTResourceManager.getImage(MainWindow.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/GoRtlHS.png"));
		FormData fd_btnInfoBrowserBack = new FormData();
		fd_btnInfoBrowserBack.bottom = new FormAttachment(0, 29);
		fd_btnInfoBrowserBack.top = new FormAttachment(0, 1);
		fd_btnInfoBrowserBack.right = new FormAttachment(100, -32);
		fd_btnInfoBrowserBack.left = new FormAttachment(100, -60);
		btnInfoBrowserBack.setLayoutData(fd_btnInfoBrowserBack);
		
		final Button btnInfoBrowserForward = new Button(cmpInfo, SWT.FLAT);
		btnInfoBrowserForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				display.asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						cmpInfoBrowser.forward();
					}
				});
			}
		});
		btnInfoBrowserForward.setEnabled(false);
		btnInfoBrowserForward.setImage(SWTResourceManager.getImage(MainWindow.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/GoLtrHS.png"));
		FormData fd_btnInfoBrowserForward = new FormData();
		fd_btnInfoBrowserForward.bottom = new FormAttachment(0, 29);
		fd_btnInfoBrowserForward.top = new FormAttachment(0, 1);
		fd_btnInfoBrowserForward.right = new FormAttachment(100, -1);
		fd_btnInfoBrowserForward.left = new FormAttachment(100, -29);
		btnInfoBrowserForward.setLayoutData(fd_btnInfoBrowserForward);
		
		cmpInfoBrowser = new Browser(cmpInfo, SWT.BORDER);
		cmpInfoBrowser.setJavascriptEnabled(false);
		cmpInfoBrowser.addLocationListener(new LocationAdapter()
		{
			@Override
			public void changed(LocationEvent event)
			{
				  btnInfoBrowserForward.setEnabled(cmpInfoBrowser.isForwardEnabled());
				  btnInfoBrowserBack.setEnabled(cmpInfoBrowser.isBackEnabled());
			}
		});
		FormData fd_cmpInfoBrowser = new FormData();
		fd_cmpInfoBrowser.top = new FormAttachment(cmpInfoUser);
		fd_cmpInfoBrowser.bottom = new FormAttachment(100);
		fd_cmpInfoBrowser.left = new FormAttachment(cmpInfoUser, 0, SWT.LEFT);
		fd_cmpInfoBrowser.right = new FormAttachment(100);
		cmpInfoBrowser.setLayoutData(fd_cmpInfoBrowser);
		
		TabItem tabReceiving = new TabItem(tabs, SWT.NONE);
		tabReceiving.setText("Receiving");
		
		xcmpReceiving = new ReceivingComposite(tabs, SWT.NONE);
		tabReceiving.setControl(xcmpReceiving);
		RowLayout rl_cmpReceiving = new RowLayout(SWT.VERTICAL);
		rl_cmpReceiving.wrap = false;
		
		TabItem tabAssignment = new TabItem(tabs, SWT.NONE);
		tabAssignment.setText("Assignment");
		
		xcmpAssignment = new AssignmentComposite(tabs, SWT.NONE);
		tabAssignment.setControl(xcmpAssignment);
		
		TabItem tabProcessing = new TabItem(tabs, SWT.NONE);
		tabProcessing.setText("Processing");
		
		Composite cmpProcessing = new Composite(tabs, SWT.NONE);
		tabProcessing.setControl(cmpProcessing);
		
		TabItem tabOutput = new TabItem(tabs, SWT.NONE);
		tabOutput.setText("Output");
		
		Composite cmpOutput = new Composite(tabs, SWT.NONE);
		tabOutput.setControl(cmpOutput);
		
		TabItem tabSettings = new TabItem(tabs, SWT.NONE);
		tabSettings.setText("Settings");
		
		Composite cmpSettings = new Composite(tabs, SWT.NONE);
		tabSettings.setControl(cmpSettings);
		cmpSettings.setLayout(new GridLayout(4, false));
		
		Label lblSqliteDb = new Label(cmpSettings, SWT.NONE);
		lblSqliteDb.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSqliteDb.setText("SQLite Database");
		
		txtSqliteDb = new Text(cmpSettings, SWT.BORDER);
		txtSqliteDb.setText("{SqliteDbFilePath}");
		txtSqliteDb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.defaultColors.put("txtSqliteDb.background", txtSqliteDb.getBackground());
		txtSqliteDb.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				Program.checkSqliteDb(self, e);
			}
		});
		
		Button btnSqliteDbBrowse = new Button(cmpSettings, SWT.NONE);
		btnSqliteDbBrowse.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.browseSqliteDb(self, e);
			}
		});
		btnSqliteDbBrowse.setText("Browse");
		
		Button btnSqliteDbSet = new Button(cmpSettings, SWT.NONE);
		btnSqliteDbSet.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.setSqliteDb(self, e);
			}
		});
		btnSqliteDbSet.setText("Set");
		
		Label lblFileServer = new Label(cmpSettings, SWT.NONE);
		lblFileServer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileServer.setText("File Server");
		
		txtFileServer = new Text(cmpSettings, SWT.BORDER);
		txtFileServer.setText("{FileServerPath}");
		txtFileServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.defaultColors.put("txtFileServer.background", txtFileServer.getBackground());
		txtFileServer.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				Program.checkFileServer(self, e);
			}
		});
		
		Button btnFileServerBrowse = new Button(cmpSettings, SWT.NONE);
		btnFileServerBrowse.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.browseFileServer(self, e);
			}
		});
		btnFileServerBrowse.setText("Browse");
		
		Button btnFileServerSet = new Button(cmpSettings, SWT.NONE);
		btnFileServerSet.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.setFileServer(self, e);
			}
		});
		btnFileServerSet.setText("Set");
				
		Label lblLocalCopy = new Label(cmpSettings, SWT.NONE);
		lblLocalCopy.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLocalCopy.setText("Local Copy");
		
		txtLocalCopy = new Text(cmpSettings, SWT.BORDER);
		txtLocalCopy.setText("{LocalCopyPath}");
		txtLocalCopy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.defaultColors.put("txtLocalCopy.background", txtLocalCopy.getBackground());
		txtLocalCopy.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				Program.checkLocalCopy(self, e);
			}
		});
		
		Button btnLocalCopyBrowse = new Button(cmpSettings, SWT.NONE);
		btnLocalCopyBrowse.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.browseLocalCopy(self, e);
			}
		});
		btnLocalCopyBrowse.setText("Browse");
		
		Button btnLocalCopySet = new Button(cmpSettings, SWT.NONE);
		btnLocalCopySet.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.setLocalCopy(self, e);
			}
		});
		btnLocalCopySet.setText("Set");
		
	}
	
	protected void close(ShellEvent e)
	{
		System.out.printf("MainWindow.close(ShellEvent) ");
		Utilities.dump(e, System.out);
	}
	
	public Display getDisplay()
	{
		return this.display;
	}
	
	public Shell getShell()
	{
		return this.shell;
	}
	
	public Text getTextSqliteDb()
	{
		return this.txtSqliteDb;
	}
	
	public Text getTextFileServer()
	{
		return this.txtFileServer;
	}
	
	public Text getTextLocalCopy()
	{
		return this.txtLocalCopy;
	}
	
	public Color getDefaultColor(String name)
	{
		return this.defaultColors.get(name);
	}
	
	public Color getColor_BG_INVALID_SETTING()
	{
		return new Color(this.display, 255, 204, 204);
	}
	
	public Color getColor_BG_VALID_SETTING()
	{
		return new Color(this.display, 204, 255, 204);
	}
	
	public Color getColor_BG_CHANGED_SETTING()
	{
		return new Color(this.display, 255, 255, 225);
	}
	
	public String getTitle()
	{
		return this.shell.getText();
	}
	
	/**
	 * <p>WARNING: This method asynchronously modifies a UI element.</p>
	 */
	public void setTitle(final String title)
	{
		this.display.asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				shell.setText(title);
			}
		});
	}

	public void setInfoUrl(String string)
	{
		this.cmpInfoBrowser.setUrl(string);
		
	}
}
