package com.example.IntegratedCA.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {


	private static final long serialVersionUID = -6533195849813184042L;

	public BadRequestException(String message) {
		super(message);
	}
	
}
