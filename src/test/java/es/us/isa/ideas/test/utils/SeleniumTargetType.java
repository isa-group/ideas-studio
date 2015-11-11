package es.us.isa.ideas.test.utils;

public enum SeleniumTargetType {
	
	LOCAL_FILE("selenium-local.properties"),
	REMOTE_FILE("selenium-remote.properties");
	

	private final String fileName;
	
	private SeleniumTargetType(final String name) {
		this.fileName = name;
	}
	
	public String toString() {
		return fileName;
	}

}
