package project.annotations;

public class UserComputeAPIPrototype {
    private String inputSource = "default_input";
    private String outputDestination = "default_output";
    private String delimiters = ",";
    
    @Override
    public void setInputSource(String source) { this.inputSource = source; }
    
    @Override
    public void setOutputDestination(String destination) { this.outputDestination = destination; }
    
    @Override
    public void setDelimiters(String delimiters) { this.delimiters = delimiters; }
    
    @Override
    public String processRequest() { return "Processing request with prototype"; }
    }
    
    
