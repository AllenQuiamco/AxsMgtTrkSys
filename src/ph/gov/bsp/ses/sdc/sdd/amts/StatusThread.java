package ph.gov.bsp.ses.sdc.sdd.amts;

import ph.gov.bsp.ses.sdc.sdd.amts.ui.StatusDialog;

/**
 * Contains the task run by a StatusDialog instance, and the defined exit code
 * of the thread.
 * 
 * Subclasses of <code>StatusThread</code> should override the
 * <code>run()</code> method.
 * 
 * The exit code may be set in the overriden <code>run()</code> method.
 * 
 * IMPORTANT: The {@link StatusDialog} may interrupt <code>StatusThread</code>
 * instances. Handle {@link Thread#interrupt()} within potentially
 * non-terminating loops by checking {@link Thread#interrupted()} or by using
 * {@link Thread#sleep(long)} and catching {@link InterruptedException}.
 * 
 * @author © 2017 Miguel Aguinaldo
 * 
 * @see StatusDialog
 * 
 */
public abstract class StatusThread extends Thread
{
	protected StatusDialog statusDialog;
	private int exitCode = ExitCode.THREAD_NOT_DONE;
	
	/**
	 * Constructs a <code>StatusThread</code> instance with a specified
	 * <code>StatusDialog</code> instance.
	 * @param statusDialog A <code>StatusDialog</code> instance. This allows
	 * overrides of the <code>run()</code> method to access the
	 * <code>StatusDialog</code> instance it will be used in.
	 */
	public StatusThread(StatusDialog statusDialog)
	{
		this.statusDialog = statusDialog;
	}
	
	/**
	 * Gets the {@link ExitCode} of this <code>StatusThead</code> instance.
	 * Upon instantiation, the default value is {@link ExitCode#THREAD_NOT_DONE}
	 * .
	 * @return An integer
	 */
	public int getExitCode()
	{
		return exitCode;
	}
	
	/**
	 * Sets the {@link ExitCode} of this <code>StatusThead</code> instance.
	 * @param exitCode
	 */
	public void setExitCode(int exitCode)
	{
		this.exitCode = exitCode;
	}
	
	/**
	 * Test
	 */
	@Override
	public void run()
	{
		super.run();
	}
}
