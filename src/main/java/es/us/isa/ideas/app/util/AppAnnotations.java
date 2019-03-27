package es.us.isa.ideas.app.util;

import java.io.Serializable;

public class AppAnnotations implements Serializable {
	
	private static final long serialVersionUID = 4145457488472708296L;
	
	public class Type {

		public static final String INFO = "info";
		public static final String WARNING = "warning";
		public static final String ERROR = "error";
		public static final String FATAL = "fatal";
    };

	protected String row;
	protected String column;
	protected String text;
	protected String type;
	
	public AppAnnotations() {
		super();
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
