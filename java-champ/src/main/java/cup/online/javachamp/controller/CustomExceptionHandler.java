package cup.online.javachamp.controller;

import cup.online.javachamp.dto.ApiError;
import cup.online.javachamp.exception.ObjectAlreadyExistException;
import cup.online.javachamp.exception.ObjectNotFoundException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;

import static java.util.Objects.nonNull;

//@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException ex, WebRequest request) {
        return new ResponseEntity<Object>(
                ApiError.builder()
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .path(((ServletWebRequest)request).getRequest().getRequestURI())
                        .requestId(
                                nonNull(MDC.get("requestId")) ?
                                        MDC.get("requestId") :
                                        "-")
                        .timestamp(OffsetDateTime.now())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler({HttpMessageNotReadableException.class})
//    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
//        return new ResponseEntity<Object>(
//                ApiError.builder()
//                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
//                        .status(HttpStatus.BAD_REQUEST.value())
//                        .path(((ServletWebRequest)request).getRequest().getRequestURI())
//                        .requestId(
//                                nonNull(MDC.get("requestId")) ?
//                                        MDC.get("requestId") :
//                                        "-")
//                        .timestamp(OffsetDateTime.now())
//                        .build(),
//                HttpStatus.BAD_REQUEST);
//    }
}
