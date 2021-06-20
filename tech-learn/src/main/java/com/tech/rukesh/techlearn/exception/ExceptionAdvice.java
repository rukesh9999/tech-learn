package com.tech.rukesh.techlearn.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tech.rukesh.techlearn.dto.ErrorDto;

@RestControllerAdvice
public class ExceptionAdvice {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException manv)
	{
		Map<String, String> errorsMap =new LinkedHashMap<>();
		manv.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName= ((FieldError)error).getField();
			String errorMessage =error.getDefaultMessage();
			errorsMap.put(fieldName, errorMessage);
		});
		return new ResponseEntity<Map<String, String>>(errorsMap,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TechnoloyException.class)
	public ResponseEntity<ErrorDto> handleTechnoloyException(TechnoloyException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}

	
	@ExceptionHandler(NoSuchTechnoloyExistsException.class)
	public ResponseEntity<ErrorDto> handleNoSuchTechnoloyExistsException(NoSuchTechnoloyExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}

	
	@ExceptionHandler(TechnoloyAlreadyExistsException.class)
	public ResponseEntity<ErrorDto> handleTechnoloyAlreadyExistsException(TechnoloyAlreadyExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(StatusMainException.class)
	public ResponseEntity<ErrorDto> handleStatusMainException(StatusMainException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoSuchStatusMainException.class)
	public ResponseEntity<ErrorDto> handleNoSuchStatusMainException(NoSuchStatusMainException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(StatusMainAlreadyExists.class)
	public ResponseEntity<ErrorDto> handleStatusMainAlreadyExists(StatusMainAlreadyExists te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorDto> handleUserAlreadyExistsException(UserAlreadyExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoSuchUserExistsException.class)
	public ResponseEntity<ErrorDto> handleNoSuchUserExistsException(NoSuchUserExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.NOT_FOUND);
	}
	
	
	
	
	
}