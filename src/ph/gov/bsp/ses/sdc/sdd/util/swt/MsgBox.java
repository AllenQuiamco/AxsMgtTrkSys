package ph.gov.bsp.ses.sdc.sdd.util.swt;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public final class MsgBox
{
	public static MsgBoxResult show(Shell shell, String message, String caption, int style)
	{
		MessageBox msgbox = new MessageBox(shell, style);
		msgbox.setMessage(message);
		msgbox.setText(caption);
		int result = msgbox.open();
		return MsgBoxResult.FromInteger(result);
	}
	
	public static MsgBoxResult show(Shell shell, String message, String caption, MsgBoxButtons buttons, MsgBoxIcon icon)
	{
		return show(shell, message, caption, buttons.toInteger() | icon.toInteger());
	}
	
	public static MsgBoxResult show(Shell shell, String message, String caption, MsgBoxButtons buttons)
	{
		return show(shell, message, caption, buttons.toInteger());
	}
	
	public static MsgBoxResult show(Shell shell, String message, String caption)
	{
		return show(shell, message, caption, MsgBoxButtons.OK);
	}
	
	public static MsgBoxResult show(Shell shell, String message)
	{
		return show(shell, message, "Alert", MsgBoxButtons.OK);
	}
	
	public static MsgBoxResult show(String message, String caption)
	{
		return show(new Shell(), message, caption, MsgBoxButtons.OK);
	}
	
	public static MsgBoxResult show(String message, String caption, MsgBoxButtons buttons, MsgBoxIcon icon)
	{
		return show(new Shell(), message, caption, buttons.toInteger() | icon.toInteger());
	}
	
	public static MsgBoxResult show(String message, String caption, MsgBoxButtons buttons)
	{
		return show(new Shell(), message, caption, buttons.toInteger());
	}
	
	public static MsgBoxResult show(String message)
	{
		return show(new Shell(), message, "Alert", MsgBoxButtons.OK);
	}
}
