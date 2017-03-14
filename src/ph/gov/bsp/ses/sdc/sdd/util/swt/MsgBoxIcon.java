package ph.gov.bsp.ses.sdc.sdd.util.swt;

import org.eclipse.swt.SWT;

public enum MsgBoxIcon
{
	ERROR(SWT.ICON_ERROR),
	INFORMATION(SWT.ICON_INFORMATION),
	QUESTION(SWT.ICON_QUESTION),
	WARNING(SWT.ICON_WARNING),
	WORKING(SWT.ICON_WORKING),
	// CANCEL (SWT.ICON_CANCEL),
	// SEARCH (SWT.ICON_SEARCH),
	;
	
	protected int IntValue = 0;
	
	MsgBoxIcon(int value)
	{
		this.IntValue = value;
	}
	
	public int toInteger()
	{
		return this.IntValue;
	}
}
