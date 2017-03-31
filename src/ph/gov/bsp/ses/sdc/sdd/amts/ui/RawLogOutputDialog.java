package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class RawLogOutputDialog extends Dialog
{
	protected static final String TAG_WINDOW_STATE = "ui.rawlogoutputdialog.windowstate";
	protected boolean result;
	protected Shell shell;
	private Display display;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RawLogOutputDialog(Shell parent, int style)
	{
		super(parent, style);
		display = getParent().getDisplay();
		createContents();		
		setWindowState(Program.getSetting(TAG_WINDOW_STATE));
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean open()
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
		return result;
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents()
	{
		shell = new Shell(getParent(), SWT.CLOSE | SWT.MIN | SWT.TITLE | SWT.APPLICATION_MODAL);
		shell.setImage(SWTResourceManager.getImage(RawMonitoringOutputDialog.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/java-16x16-32bit.png"));
		shell.setMinimumSize(544, 138);
		shell.setSize(544, 138);
		shell.setText("Raw Output (CSV)");
		shell.setLayout(new FormLayout());
		shell.addShellListener(new ShellAdapter()
		{
			@Override
			public void shellClosed(ShellEvent e)
			{
				String windowState = getWindowState();
				Program.setSetting(TAG_WINDOW_STATE, windowState);
			}
		});
		
		RawLogOutputGroup rwlgtptgrpRawDatacsv = new RawLogOutputGroup(shell, SWT.NONE);
		rwlgtptgrpRawDatacsv.setText("Log");
		FormData fd_rwlgtptgrpRawDatacsv = new FormData();
		fd_rwlgtptgrpRawDatacsv.right = new FormAttachment(100, -1);
		fd_rwlgtptgrpRawDatacsv.left = new FormAttachment(0, 1);
		fd_rwlgtptgrpRawDatacsv.bottom = new FormAttachment(100, -1);
		fd_rwlgtptgrpRawDatacsv.top = new FormAttachment(0, 1);
		rwlgtptgrpRawDatacsv.setLayoutData(fd_rwlgtptgrpRawDatacsv);
		
	}

	protected void setWindowState(String string)
	{
		if (Utilities.isNullOrBlank(string)) return;
		
		String[] split = string.trim().split("\\s+");
		String state = Utilities.getArrayItem(split, 0); if (state == null) state = ""; state = state.toUpperCase();
		String posX = Utilities.getArrayItem(split, 1);
		String posY = Utilities.getArrayItem(split, 2);
		String sizeX = Utilities.getArrayItem(split, 3);
		String sizeY = Utilities.getArrayItem(split, 4);
		
		if (state.equals("MAXIMIZED"))
		{
			shell.setMaximized(true);
			//shell.setMinimized(false);
		}
		else if (state.equals("RESTORED"))
		{
			shell.setMaximized(false);
			shell.setMinimized(false);
			
			if ((posX != null) && (posY != null))
			{
				try
				{
					int x = Integer.parseInt(posX);
					int y = Integer.parseInt(posY);
					
					if (x < 0) x = 0;
					if (y < 0) y = 0;
					
					shell.setLocation(new Point(x, y));
				}
				catch (NumberFormatException e) { }
			}
			
			if ((sizeX != null) && (sizeY != null))
			{
				try
				{
					int x = Integer.parseInt(sizeX);
					int y = Integer.parseInt(sizeY);
					
					if (x < 0) x = 0;
					if (y < 0) y = 0;
					
					shell.setSize(x, y);
				}
				catch (NumberFormatException e) { }
			}
		}
	}
	
	protected String getWindowState()
	{
		boolean max = shell.getMaximized();
		boolean min = shell.getMinimized();
		Point location = shell.getLocation();
		Point size = shell.getSize();
		String state = "";
		if (max && min) state = "MAXIMIZED";
		else if(max && !min) state = "MAXIMIZED";
		else if(!max && min) state = "RESTORED";
		else state = "RESTORED";
		return String.format("%s %d %d %d %d", state, location.x, location.y, size.x, size.y);
	}
}
