package api;

import project.annotations.ConceptualAPI;

@ConceptualAPI
public interface ComputationAPI {
    ComputationResult compute(String inputData, String[] delimiters);
}

