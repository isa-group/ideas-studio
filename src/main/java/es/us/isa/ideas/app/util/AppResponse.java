package es.us.isa.ideas.app.util;

import java.io.Serializable;

public class AppResponse implements Serializable {
	
	private static final long serialVersionUID = 7249763318311372766L;
	
	public enum Status {
		
		OK("OK"), OK_PROBLEMS("OK_PROBLEMS"), ERROR("ERROR"), SESSION_ERROR("SESSION_ERROR");
		
		private String val ;
		Status(String val) { this.val = val; }		
		@Override public String toString() { return this.val; }
    };
	
	protected Status status;
	protected String message;
	protected String htmlMessage;		 // Optional
	protected String data;
	protected String context;			 // Optional
	protected String fileUri;			 // Optional
	protected AppAnnotations[] annotations;
	protected Serializable customStruct; // Optional
	
	public AppResponse() {
		super();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHtmlMessage() {
		return htmlMessage;
	}

	public void setHtmlMessage(String htmlMessage) {
		this.htmlMessage = htmlMessage;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	public AppAnnotations[] getAnnotations() {
		return annotations;
	}

	public void setAnnotations(AppAnnotations[] annotations) {
		this.annotations = annotations;
	}

	public Serializable getCustomStruct() {
		return customStruct;
	}

	public void setCustomStruct(Serializable customStruct) {
		this.customStruct = customStruct;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	
	

}
