package es.us.isa.ideas.repo.exception;

public class BadUriException extends Exception {

	private static final long serialVersionUID = -8911388402160040500L;

	public BadUriException( String message ) {
		super(message);
	}
	
	public BadUriException() {
		super();
	}
	
}
