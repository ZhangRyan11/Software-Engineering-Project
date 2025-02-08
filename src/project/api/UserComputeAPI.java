package project.api;

import project.annotations.NetworkAPI;

@NetworkAPI
public interface UserComputeAPI {
    ComputeResponse processData(ComputeRequest request);
}
package project.api;

public class UserComputeAPI {
    
}
