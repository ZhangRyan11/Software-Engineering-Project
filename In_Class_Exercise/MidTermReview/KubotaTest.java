import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class KubotaTest {
  
    @Test
    public void smokeTest() {
        // create mocks
        FrontendLoader mockLoader= Mockito.mock(FrontendLoader.class);
        Backhoe mockBackhoe= Mockito.mock(Backhoe.class);
        
        // configure mocks
        when(mockLoader.engage()).thenReturn(true);
        
        Kubota actualKubota = new Kubota(mockLoader, mockBackhoe);
        
        actualKubota.scoopDirt();
        
        Scoop result = actualKubota.dumpScoop();
        
        Assertions.assertNotNull(result);
    }
}
