package project.annotations;

public class WrappedHashMap {
private map<Integer, String> data;
private boolean needToInitialize;

public string get(int id)
{
	if(needToInitialize)
	{
		initialize();
		needToInitialize = false;
	}
	return data.get(id);
}
public void put(int id, string value)
{
	if(needToInitialize)
	{
		initialize();
		needToInitialize = false;
	}
	return data.get(id);
}
data.put(id, value);
}
