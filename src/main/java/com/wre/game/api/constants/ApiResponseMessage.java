package com.wre.game.api.constants;

public class ApiResponseMessage {
	// HttpStatus
	private String status;
	// Http Default Message
	private String message;
	// Error Message to USER
	private String errorMessage;
	// Error Code
	private String errorCode;



	@Override
	public String toString() {
		return "ApiResponseMessage{" +
				"status='" + status + '\'' +
				", message='" + message + '\'' +
				", errorMessage='" + errorMessage + '\'' +
				", errorCode='" + errorCode + '\'' +
				'}';
	}



	public ApiResponseMessage() {}

	public ApiResponseMessage(RtCode rtCode) {
		this.status = String.valueOf(rtCode.getHttpStatus().value());
		this.message = rtCode.getErrorMessage();
		this.errorCode = rtCode.getErrorCode();
		this.errorMessage = rtCode.getErrorMessage();
	}

	public ApiResponseMessage(String status, String message, String errorCode, String errorMessage) {
		this.status = status;
		this.message = message;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
