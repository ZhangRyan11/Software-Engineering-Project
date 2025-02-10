package project.annotations;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

public class ExampleMockito {
	
	public static interface Mockable{
		int length();
	}
	
	public static class StringWrapper{
		private final Mockable innerString; 
		
		public StringWrapper(Mockable innerString) {
			this.innerString = innerString; 
		}
		
		public int calculate() {
			return innerString.length();
		}
	}
	
	@Test
	public void demoMocks() {
		ExampleMockito.Mockable dependency = mock(Mockable.class);
		when(dependency.length()).thenReturn(10); 
		
		StringWrapper wrapper = new StringWrapper(dependency);
		if(wrapper.calculate() != 10) {
			throw new RuntimeException(); 
		}
	}

}
