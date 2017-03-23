package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

import ph.gov.bsp.ses.sdc.sdd.amts.ExitCode;
import ph.gov.bsp.ses.sdc.sdd.amts.StatusThread;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBox;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxButtons;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxResult;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Rectangle;

public class StatusDialog extends Dialog
{
	protected Object result;
	public Shell shell;
	private StatusThread statusThread;
	private boolean closeIfThreadIsDone;
	private Text textbox;
	private Display display;
	private Queue<String> updateQueue = new ConcurrentLinkedQueue<String>();
	private Thread waitThread;
	private Thread updateThread;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public StatusDialog(Shell parent, int style)
	{
		super(parent, style);
		setText("SWT Dialog");
	}
	
	/**
	 * Sets this <code>StatusDialog</code>'s {@link StatusThread} instance.
	 * @param statusThread
	 */
	public void setStatusThread(StatusThread statusThread)
	{
		this.statusThread = statusThread;
	}
	
	/**
	 * Sets the behavior of the <code>StatusDilaog</code> once the given
	 * {@link StatusThread} is done. If <code>true</code>, the
	 * <code>StatusDialog</code> will close.
	 * @param closeIfThreadIsDone
	 */
	public void setCloseIfThreadIsDone(boolean closeIfThreadIsDone)
	{
		this.closeIfThreadIsDone = closeIfThreadIsDone;
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open()
	{
		display = getParent().getDisplay();
		createContents();
		
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		shell.open();
		shell.layout();
		startThreads();
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shell.addListener(SWT.Close, new Listener()
		{
			
			/**
			 * Handles a close window event
			 */
			@Override
			public void handleEvent(Event event)
			{
				if (statusThread.getExitCode() == ExitCode.THREAD_NOT_DONE)
				{
					if (MsgBox.show("Close?", "Close", MsgBoxButtons.YES_NO) == MsgBoxResult.YES)
					{
						statusThread.setExitCode(ExitCode.USER_ABORTED);
						statusThread.interrupt();
						updateThread.interrupt();
						event.doit = true;
					}
					else
					{
						event.doit = false;
					}
				}
			}
		});
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		textbox = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textbox.setCursor(display.getSystemCursor(SWT.CURSOR_ARROW));
		textbox.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				textbox.setTopIndex(textbox.getLineCount() - 1);
			}
		});
		textbox.setText("{text}");
	}
	
	private void startThreads()
	{
		
		// thread to wait for the StatusThread to finish
		this.waitThread = new Thread()
		{
			@Override
			public void run()
			{
				while (statusThread.isAlive() && !statusThread.isInterrupted())
				{
					// System.out.println(String.format("StatusDialog.startThreads()#waitThread.run()#while %08x",
					// new java.util.Random().nextInt()));
				}
				if (statusThread.getExitCode() == ExitCode.THREAD_NOT_DONE)
				{
					statusThread.setExitCode(ExitCode.SUCCESS);
				}
				if (closeIfThreadIsDone)
				{
					if (display == null || display.isDisposed()) return;
					display.asyncExec(new Runnable()
					{
						
						@Override
						public void run()
						{
							if (!shell.isDisposed()) shell.close();
						}
					});
				}
			}
		};
		
		// thread to update the textbox
		this.updateThread = new Thread()
		{
			
			@Override
			public void run()
			{
				setText("");
				while (!Thread.interrupted() && !shell.isDisposed())
				{
					// System.out.println(String.format("StatusDialog.startThreads()#updateThread.run()#while %08x",
					// new java.util.Random().nextInt()));
					final String text = updateQueue.poll();
					if (text != null) appendText(text);
				}
			}
		};
		
		statusThread.start();
		updateThread.start();
		waitThread.start();
	}
	
	public void setTitle(final String title)
	{
		if (display == null || display.isDisposed()) return;
		display.asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				if (!shell.isDisposed()) shell.setText(title);
			}
		});
	}
	
	public void appendText(final String text)
	{
		if (display == null || display.isDisposed()) return;
		display.asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				if (!textbox.isDisposed()) textbox.append(text);
			}
		});
	}
	
	@Override
	public void setText(final String text)
	{
		if (display == null || display.isDisposed()) return;
		display.asyncExec(new Runnable()
		{
			
			@Override
			public void run()
			{
				if (!textbox.isDisposed()) textbox.setText(text);
			}
		});
	}
	
	/**
	 * Adds text to the <code>StatusDialog</code>'s display.
	 * @param caption
	 */
	public void update(String text)
	{
		this.updateQueue.add(text);
	}
	
	/**
	 * Gets the {@link ExitCode} of the underlying {@link StatusThread}.
	 * @return
	 */
	public int getThreadResult()
	{
		return statusThread.getExitCode();
	}
	
}
