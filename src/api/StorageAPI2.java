package api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import project.annotations.ProcessAPI;


@ProcessAPI
public interface StorageAPI2
{
	String readData(String location);
	void writeData(String location, String content);
}