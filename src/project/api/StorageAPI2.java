import java.lang.*;
@Retention(RetentionPolicy.RUNTIME)
@interface ProcessAPI{}

@ProcessAPI
public interface storageAPI
{
	String readData(String location);
	void writeData(String location, String content);
}