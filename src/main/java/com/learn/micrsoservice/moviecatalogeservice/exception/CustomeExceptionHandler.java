package com.learn.micrsoservice.moviecatalogeservice.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

public class CustomeExceptionHandler extends ResponseEntityExceptionHandler {

    Logger LOGGER = LogManager.getLogger(CustomeExceptionHandler.class.getName());

    //400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        final List<String> errors = new ArrayList<>();
        for(final FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.add(error.getField()+" , " + error.getDefaultMessage());
        }

        for(final ObjectError error: ex.getBindingResult().getGlobalErrors()){
            errors.add(error.getObjectName() + " , " + error.getDefaultMessage());
        }

        final ExceptionResponse apiError = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getHttpStatus(), request);
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = new ArrayList<>();
        for(final FieldError error : ex.getBindingResult().getFieldErrors())
        {
            errors.add(error.getField() + " , " + error.getDefaultMessage());
        }

        for(final ObjectError error : ex.getBindingResult().getGlobalErrors()){
            errors.add(error.getObjectName() + " , " + error.getDefaultMessage());
        }

        final ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(),errors);
        return handleExceptionInternal(ex,response,headers,response.getHttpStatus(),request);

    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be type of " + ex.getRequiredType();

        final ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(),error);

        return new ResponseEntity<Object>(response,new HttpHeaders(),response.getHttpStatus());
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final String error = ex.getRequestPartName() + " part is missing";
        final ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(response, new HttpHeaders(), response.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String error = ex.getParameterName() + " parameter is missing";
        final ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(response, new HttpHeaders(), response.getHttpStatus());
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        final ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(response, new HttpHeaders(), response.getHttpStatus());
    }

    // 403
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        System.out.println("request" + request.getUserPrincipal());
        return new ResponseEntity<Object>("Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }


    //404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
         final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(response, new HttpHeaders(), response.getHttpStatus());
    }

    //500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        final ExceptionResponse apiError = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    //500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


}
