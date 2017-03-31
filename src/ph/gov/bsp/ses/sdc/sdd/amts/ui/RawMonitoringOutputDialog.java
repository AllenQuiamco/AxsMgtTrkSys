package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

public class RawMonitoringOutputDialog extends Dialog
{
	protected static final String TAG_WINDOW_STATE = "ui.rawmonitoringoutputdialog.windowstate";
	protected boolean result;
	protected Shell shell;
	private Display display;
	
	public static void main(String[] args)
	{
		RawMonitoringOutputDialog dialog = new RawMonitoringOutputDialog(new Shell(), SWT.NONE);
		dialog.open();
	}
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RawMonitoringOutputDialog(Shell parent, int style)
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
		shell = new Shell(getParent(), SWT.CLOSE | SWT.MIN | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		shell.setImage(SWTResourceManager.getImage(RawMonitoringOutputDialog.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/java-16x16-32bit.png"));
		shell.setMinimumSize(581, 300);
		shell.setSize(581, 300);
		shell.setText("Raw Output (CSV)");
		shell.setLayout(new FormLayout());
		shell.addControlListener(new ControlAdapter()
		{
			@Override
			public void controlResized(ControlEvent e)
			{
				int height = shell.getSize().y;
				shell.setSize(shell.getMinimumSize().x, height);
			}
		});
		shell.addShellListener(new ShellAdapter()
		{
			@Override
			public void shellClosed(ShellEvent e)
			{
				String windowState = getWindowState();
				Program.setSetting(TAG_WINDOW_STATE, windowState);
			}
		});
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(true);
		FormData fd_scrolledComposite = new FormData();
		fd_scrolledComposite.right = new FormAttachment(100, 0);
		fd_scrolledComposite.bottom = new FormAttachment(100, 0);
		fd_scrolledComposite.top = new FormAttachment(0, 0);
		fd_scrolledComposite.left = new FormAttachment(0, 0);
		scrolledComposite.setLayoutData(fd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		
		RawMonitoringOutputGroup xgrpMonitoring = new RawMonitoringOutputGroup(scrolledComposite, SWT.NONE);
		xgrpMonitoring.setText("Monitoring");
		scrolledComposite.setContent(xgrpMonitoring);
		scrolledComposite.setMinSize(xgrpMonitoring.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
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
