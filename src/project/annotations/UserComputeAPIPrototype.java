package project.annotations;

public class UserComputeAPIPrototype {
    private String inputSource = "default_input";
    private String outputDestination = "default_output";
    private String delimiters = ",";
    
    public void setInputSource(String source) { this.inputSource = source; }
    
    public void setOutputDestination(String destination) { this.outputDestination = destination; }
    
    public void setDelimiters(String delimiters) { this.delimiters = delimiters; }
    
    public String processRequest() { return "Processing request with prototype"; }
    }
    
    
