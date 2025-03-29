package api;

import java.util.List;

public interface ComputationResult {
    boolean isSuccess();
    List<Integer> getFactors();
}
