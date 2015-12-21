package es.us.isa.ideas.test.utils;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
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
