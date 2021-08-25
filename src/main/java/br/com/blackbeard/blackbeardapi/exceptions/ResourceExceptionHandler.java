package br.com.blackbeard.blackbeardapi.exceptions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError<Map<String, String>>> validation(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        var errors = new HashMap<String, String>();

        e.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(),
                fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : ""));

        return ResponseEntity.badRequest().body(new StandardError<>(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                "Validation error",
                Calendar.getInstance().getTimeInMillis(),
                request.getRequestURI()));
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardError<String>> file(
            FileException e, HttpServletRequest request) {
        var exception = new StandardError<>(
                HttpStatus.BAD_REQUEST.value(),
                "error of archive",
                e.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<StandardError<String>> amazonService(
            AmazonServiceException e, HttpServletRequest request) {
        HttpStatus code = HttpStatus.valueOf(e.getErrorCode());
        var exception =
                new StandardError<>(
                        code.value(),
                        "Error amazon service",
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(code.value()).body(exception);
    }

    @ExceptionHandler(AmazonClientException.class)
    public ResponseEntity<StandardError<String>> amazonClient(
            AmazonClientException e, HttpServletRequest request) {
        var exception =
                new StandardError<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Error amazon client",
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<StandardError<String>> amazonS3(
            AmazonS3Exception e, HttpServletRequest request) {
        var exception =
                new StandardError<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Error amazon s3",
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<StandardError<String>> upload(
            MaxUploadSizeExceededException e, HttpServletRequest request) {
        StandardError<String> exception =
                new StandardError<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Error",
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception);
    }

    @ExceptionHandler(BarberShopImageLimitException.class)
    public ResponseEntity<StandardError<String>> limit(
            BarberShopImageLimitException e, HttpServletRequest request) {
        StandardError<String> exception =
                new StandardError<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "error image",
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception);
    }

}