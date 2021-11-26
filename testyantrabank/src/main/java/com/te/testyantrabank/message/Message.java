package com.te.testyantrabank.message;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	private boolean error;
	private String message;
	private Object data;
	private int statusCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date timestamp;

	public Message(boolean error, String message) {
		this.error = error;
		this.message = message;
	}

	public Message(boolean error, String message, List<Object> data) {
		this.error = error;
		this.message = message;
		this.data = data;
	}

	public Message(int statusCode, Date timestamp, boolean error, String message, Object data) {
		this.statusCode = statusCode;
		this.timestamp = timestamp;
		this.error = error;
		this.message = message;
		this.data = data;
	}
}
