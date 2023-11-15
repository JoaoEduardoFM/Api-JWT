package br.com.config.exception;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;

import br.com.model.response.ResponseRest;
import br.com.model.response.ResponseRest.messageType;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class,
            NoSuchElementException.class, EntityNotFoundException.class })
    public ResponseEntity<Object> handleBadRequestException(Exception ex) {
        return new ResponseEntity<>(getErrorMap(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = { MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    	ResponseRest response = new ResponseRest();
		List<String> erros = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());	
		
		for (String listaErro : erros) {
			response.setMessage(listaErro);
		}

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);		
    }

    @ExceptionHandler(value = { DataAccessException.class })
    public ResponseEntity<Object> handleDataAccessException(Exception ex) {
        return new ResponseEntity<>(getErrorMap("Erro ao acessar o banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { ResourceAccessException.class, HttpServerErrorException.class })
    public ResponseEntity<Object> handleExternalServiceException(Exception ex) {
        return new ResponseEntity<>(getErrorMap("Erro ao acessar serviço externo"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorMap("Recurso não encontrado"), HttpStatus.NOT_FOUND);
    }

    private Map<String, String> getErrorMap(String message) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", message);
        return errorMap;
    }
    
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ResponseRest response = new ResponseRest();
        response.setType(messageType.ATENCAO);
        response.setMessage("Você não tem permissão para acessar este recurso. Realize o login.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
