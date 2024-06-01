package com.task.constants;

public enum StatusCodeEnum {
	Code_200(200, ""), Code_201(201, "");

	private int code;
	private String message;

	StatusCodeEnum(int code, String message) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;

	}
	
	public String getMessage() {
		return message;
	}
}
