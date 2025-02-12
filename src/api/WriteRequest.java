package api;


import service.CoordinatorService;

public class WriteRequest {

	//Will contain the result from the compute engine and the desired output location 

	String destination; 
	String writeData; 
	String delimiter; 

	//Will have to be passed the List of data to write to the desired file.
	//This will be the result of the factor, probably sent by compute engine.
	public WriteRequest(ComputeRequest computeRequest){
		this.destination = computeRequest.getDestination();
		this.writeData = "";
		this.delimiter = computeRequest.getDelimiter(); 
	}
	
	public WriteRequest(ComputeRequest computeRequest, String writeData) {
		this.destination = computeRequest.getDestination();
		this.writeData = writeData; 
		this.delimiter = computeRequest.getDelimiter();
	}
	public WriteRequest(CoordinatorService.ComputeRequest computeRequest, String writeData) {
		this.destination = computeRequest.getDestination();
		this.writeData = writeData;
		this.delimiter = computeRequest.getDelimiter();
	}


}