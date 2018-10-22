package af.web.restful;

public class AfRestfulException extends Exception
{
	public int error;
	public String reason;
	
	public AfRestfulException()
	{		
	}
	public AfRestfulException(int error, String reason)
	{
		this.error = error;
		this.reason = reason;
	}
	@Override
	public String getMessage()
	{
		return reason + "(" + error + ")";
	}
	
	
}
