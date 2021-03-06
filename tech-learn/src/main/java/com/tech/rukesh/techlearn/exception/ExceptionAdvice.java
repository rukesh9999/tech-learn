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
		return new ResponseEntity<Map<String, String>>(errorsMap,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(TechnoloyException.class)
	public ResponseEntity<ErrorDto> handleTechnoloyException(TechnoloyException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@ExceptionHandler(NoSuchTechnoloyExistsException.class)
	public ResponseEntity<ErrorDto> handleNoSuchTechnoloyExistsException(NoSuchTechnoloyExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@ExceptionHandler(TechnoloyAlreadyExistsException.class)
	public ResponseEntity<ErrorDto> handleTechnoloyAlreadyExistsException(TechnoloyAlreadyExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(StatusMainException.class)
	public ResponseEntity<ErrorDto> handleStatusMainException(StatusMainException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NoSuchStatusMainException.class)
	public ResponseEntity<ErrorDto> handleNoSuchStatusMainException(NoSuchStatusMainException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(StatusMainAlreadyExists.class)
	public ResponseEntity<ErrorDto> handleStatusMainAlreadyExists(StatusMainAlreadyExists te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorDto> handleUserAlreadyExistsException(UserAlreadyExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NoSuchUserExistsException.class)
	public ResponseEntity<ErrorDto> handleNoSuchUserExistsException(NoSuchUserExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ErrorDto> handleInvalidFormatException(InvalidFormatException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(TechLearnException.class)
	public ResponseEntity<ErrorDto> handleTechLearnException(TechLearnException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NoSuchRefreshTokenException.class)
	public ResponseEntity<ErrorDto> handleNoSuchRefreshTokenException(NoSuchRefreshTokenException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(BatchJobSettingsException.class)
	public ResponseEntity<ErrorDto> handleBatchJobSettingsException(BatchJobSettingsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NoSuchBatchJobSettingsException.class)
	public ResponseEntity<ErrorDto> handleNoSuchBatchJobSettingsException(NoSuchBatchJobSettingsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(BatchJobSettingsAlreadyExistsException.class)
	public ResponseEntity<ErrorDto> handleBatchJobSettingsAlreadyExistsException(BatchJobSettingsAlreadyExistsException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NoSuchInboxMailException.class)
	public ResponseEntity<ErrorDto> handleNoSuchInboxMailException(NoSuchInboxMailException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InboxMailException.class)
	public ResponseEntity<ErrorDto> handleInboxMailException(InboxMailException te)
	{
		ErrorDto ErrorDto =new ErrorDto(te.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorDto>(ErrorDto,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
}
