
public class Example {
	public static int computeSize(Iterable<? extends Sizeable> input) {
		int size=0;
		for(object elem: input) {
			size++;
		}
		return size;
	}
	


	
	public static class StringSize implements Sizeable{
		private String data;
		
		@Override
		public int getSize()
		{
			return data.length();
					
		}
	}
	
	public static void main(String[] args)
	{
		List<StringSize> list = new ArrayList<>();
		int totalSize = computeSize(list);
		
	}
	}

