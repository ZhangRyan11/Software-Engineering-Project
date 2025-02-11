package api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface ProcessAPI{}

@ProcessAPI
public interface StorageAPI2
{
	String readData(String location);
	void writeData(String location, String content);
}