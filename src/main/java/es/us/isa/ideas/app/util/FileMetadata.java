/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.us.isa.ideas.app.util;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author japarejo
 */
@JsonIgnoreProperties({"bytes"})
public class FileMetadata {    

	private String fileName;
	private String fileSize;
	private String fileType;

	private byte[] bytes;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
    



