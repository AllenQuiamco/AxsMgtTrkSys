package ph.gov.bsp.ses.sdc.sdd.amts;

public class UserAbortException extends Exception
{
	
	private static final long serialVersionUID = 7653210587083009880L;
	
	public UserAbortException()
	{
	}
	
	public UserAbortException(String message)
	{
		super(message);
	}
	
	public UserAbortException(Throwable cause)
	{
		super(cause);
	}
	
	public UserAbortException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
