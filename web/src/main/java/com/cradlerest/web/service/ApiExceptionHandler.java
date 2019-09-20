package com.cradlerest.web.service;

import com.cradlerest.web.controller.error.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Exception handler class for all thrown instances of {@code RestException}.
 *
 * Converts a {@code RestException} into a more suitable format before sending
 * it back as a response.
 */
@ControllerAdvice
public class ApiExceptionHandler {

	// formatting exceptions: https://stackoverflow.com/a/47918918

	/**
	 * Defines the exception response format.
	 */
	static class ErrorResponse {

		static ErrorResponse internalServerError(Throwable ex) {
			var response = new ErrorResponse();
			var status = HttpStatus.INTERNAL_SERVER_ERROR;
			response.code = status.value();
			response.error = status.getReasonPhrase();
			response.message = ex.getMessage();
			return response;
		}

		int code;
		String error;
		String message;

		private ErrorResponse() {}

		ErrorResponse(RestException ex) {
			this.code = ex.getStatus().value();
			this.error = ex.getStatus().getReasonPhrase();
			this.message = ex.getMessage();
		}

		public int getCode() {
			return code;
		}

		public String getError() {
			return error;
		}

		public String getMessage() {
			return message;
		}
	}

	@ExceptionHandler(RestException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> processRestException(RestException ex) {
		return ResponseEntity.status(ex.getStatus().value()).body(new ErrorResponse(ex));
	}
}
