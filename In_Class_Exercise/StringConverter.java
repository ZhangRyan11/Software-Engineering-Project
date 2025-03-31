
public class StringConverter {
	public static int convertString(String input) throws Exception
	{
		int result=Integer.parseInt(input);
		if(result<0||result>0)
		{
			//invalid
			throw new IllegalArgumentException("input must be 0-9 provide: " +input);
		}
		return results;
	}

}
