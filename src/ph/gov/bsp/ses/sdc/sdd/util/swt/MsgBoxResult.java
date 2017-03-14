package ph.gov.bsp.ses.sdc.sdd.util.swt;

import org.eclipse.swt.SWT;

public enum MsgBoxResult 
{
	OK(SWT.OK),
	CANCEL(SWT.CANCEL),
	YES(SWT.YES),
	NO(SWT.NO),
	RETRY(SWT.RETRY),
	ABORT(SWT.ABORT),
	IGNORE(SWT.IGNORE);
	
	protected int IntValue = 0;
	
	MsgBoxResult(int value) 
	{
		this.IntValue = value;
	}
	
	public int ToInteger() { return this.IntValue; }
	
	public boolean isOk() { return this.IntValue == OK.IntValue; }
	public boolean isCancel() { return this.IntValue == CANCEL.IntValue; }
	public boolean isYes() { return this.IntValue == YES.IntValue; }
	public boolean isNo() { return this.IntValue == NO.IntValue; }
	public boolean isRetry() { return this.IntValue == RETRY.IntValue; }
	public boolean isAbort() { return this.IntValue == ABORT.IntValue; }
	public boolean isIgnore() { return this.IntValue == IGNORE.IntValue; }
	
	public static MsgBoxResult FromInteger(int value) 
	{
		switch(value) 
		{
			case SWT.OK: return OK;
			case SWT.CANCEL: return CANCEL;
			case SWT.YES: return YES;
			case SWT.NO: return NO;
			case SWT.RETRY: return RETRY;
			case SWT.ABORT: return ABORT;
			case SWT.IGNORE: return IGNORE;
			default: throw new IllegalArgumentException("MsgBoxResult value \"" + value +"\" is not supported.");
		}
	}
}

