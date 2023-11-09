package com.pabloagustin.myfood.project.backend.payload;

public class FileResponse {

	String fileName;
	String message;

	public FileResponse(){}

	public FileResponse(String fileName, String message) {
		this.fileName = fileName;
		this.message = message;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
