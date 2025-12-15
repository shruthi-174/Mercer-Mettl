package com.mercer.mettl.auth.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(final ResourceConflictException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        exception.printStackTrace();
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

//    @ExceptionHandler(UnauthorizedAccessException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ErrorResponse handleUnauthorizedException(final UnauthorizedAccessException ex,
//                                                     final HttpServletRequest request) {
//        return new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request.getRequestURI());
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleValidationException(final MethodArgumentNotValidException ex,
//                                                   final HttpServletRequest request) {
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.toList());
//
//        String errorMessage = String.join(",", errors);
//        return new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation failed: " + errorMessage,
//                request.getRequestURI());
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ResponseBody
//    public ErrorResponse handleResourceNotFoundException(final ResourceNotFoundException ex,
//                                                         final HttpServletRequest request) {
//        return new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
//    }
//
//    @ExceptionHandler(InvalidRequestException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public final ErrorResponse handleInvalidRequestException(final InvalidRequestException ex,
//                                                             final HttpServletRequest request) {
//        return new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
//    }
}