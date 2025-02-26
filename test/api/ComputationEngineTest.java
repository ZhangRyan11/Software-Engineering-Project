package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComputationEngineTest {
    @Test
    void testFactorComputation() {
        ComputationAPI engine = new ComputationEngineImpl();
        // Choosing 6 as the number to test with (valid number input)
        ComputationResult result = engine.compute("6", new String[]{","});
        
        assertTrue(result instanceof ComputationResultImpl);
        ComputationResultImpl computeResult = (ComputationResultImpl) result;
        
        // Test part to make sure we get the proper factors being 1,2,3,6
        assertTrue(computeResult.isSuccess());
        assertTrue(computeResult.getFactors().contains(1));
        assertTrue(computeResult.getFactors().contains(2));
        assertTrue(computeResult.getFactors().contains(3));
        assertTrue(computeResult.getFactors().contains(6));
    }

    @Test
    void testInvalidInput() {
        ComputationAPI engine = new ComputationEngineImpl();
        // Testing with an invalid input for number
        ComputationResult result = engine.compute("invalid", new String[]{","});
        
        assertTrue(result instanceof ComputationResultImpl);
        ComputationResultImpl computeResult = (ComputationResultImpl) result;
        
        assertFalse(computeResult.isSuccess());
    }
}
