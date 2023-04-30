package tech.cognity.apipedidos.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import tech.cognity.apipedidos.exception.GeneralServiceException;
import tech.cognity.apipedidos.exception.NoDataFoundException;
import tech.cognity.apipedidos.exception.ValidateServiceException;
import tech.cognity.apipedidos.util.WrapperResponse;

@Slf4j
@ControllerAdvice
public class ErrorHandlerConfig extends ResponseEntityExceptionHandler{
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> all(Exception e,WebRequest request){
		log.error(e.getMessage());
		WrapperResponse<?> response = new WrapperResponse<>(false,"Internal Server Error",null);
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(ValidateServiceException.class)
	public ResponseEntity<?> validateService(Exception e,WebRequest request){
		log.info(e.getMessage());
		WrapperResponse<?> response = new WrapperResponse<>(false,e.getMessage(),null);
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<?> noDataService(Exception e,WebRequest request){
		log.info(e.getMessage());
		WrapperResponse<?> response = new WrapperResponse<>(false,e.getMessage(),null);
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(GeneralServiceException.class)
	public ResponseEntity<?> generalService(Exception e,WebRequest request){
		log.info(e.getMessage());
		WrapperResponse<?> response = new WrapperResponse<>(false,"Internal Server Error",null);
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
