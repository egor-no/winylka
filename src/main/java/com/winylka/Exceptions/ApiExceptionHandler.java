package com.winylka.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
        ErrorResponse errors = new ErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            ErrorItem error = new ErrorItem();
            error.setCode(violation.getMessageTemplate());
            error.setMessage(violation.getMessage());
            errors.addError(error);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(NoSuchOrderException.class)
    public ResponseEntity<ErrorItem> handle(NoSuchOrderException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(NoSuchReleaseInOrderException.class)
    public ResponseEntity<ErrorItem> handle(NoSuchReleaseInOrderException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(NoSuchMusicEndpointException.class)
    public ResponseEntity<ErrorItem> handle(NoSuchMusicEndpointException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(NoSuchClientException.class)
    public ResponseEntity<ErrorItem> handle(NoSuchClientException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(WrongStatusException.class)
    public ResponseEntity<?> handle(WrongStatusException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail(e.getMessage()));
    }

    public static class ErrorItem {

        @JsonInclude(JsonInclude.Include.NON_NULL) private String code;

        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public static class ErrorResponse {

        private List<ErrorItem> errors = new ArrayList<>();

        public List<ErrorItem> getErrors() {
            return errors;
        }

        public void setErrors(List<ErrorItem> errors) {
            this.errors = errors;
        }

        public void addError(ErrorItem error) {
            this.errors.add(error);
        }

    }
}