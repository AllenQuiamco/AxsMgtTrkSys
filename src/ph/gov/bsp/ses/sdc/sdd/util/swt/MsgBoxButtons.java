package ph.gov.bsp.ses.sdc.sdd.util.swt;

import org.eclipse.swt.SWT;

public enum MsgBoxButtons
{
	OK(SWT.OK),
	OK_CANCEL(SWT.OK | SWT.CANCEL),
	YES_NO(SWT.YES | SWT.NO),
	YES_NO_CANCEL(SWT.YES | SWT.NO | SWT.CANCEL),
	RETRY_CANCEL(SWT.RETRY | SWT.CANCEL),
	ABORT_RETRY_IGNORE(SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	
	protected int IntValue = 0;
	
	MsgBoxButtons(int value)
	{
		this.IntValue = value;
	}
	
	public int toInteger()
	{
		return this.IntValue;
	}
}
