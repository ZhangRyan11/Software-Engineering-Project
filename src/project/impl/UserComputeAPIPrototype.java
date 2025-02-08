package project.impl;

import project.annotations.NetworkAPIPrototype;
import project.api.*;

public class UserComputeAPIPrototype implements UserComputeAPI {
    @NetworkAPIPrototype
    public ComputeResponse processData(ComputeRequest request) {
        // Prototype implementation
        return null;
    }
}
