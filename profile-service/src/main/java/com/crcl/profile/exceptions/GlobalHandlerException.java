package com.crcl.profile.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> badRequestResponse(MethodArgumentNotValidException exception) {
        final Map<String, String> errorsMap = exception.getBindingResult().getFieldErrors()
                .stream()
                .filter(field -> nonNull(field.getDefaultMessage()))
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return new ResponseEntity<>(new ErrorResponse(errorsMap), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> bindException(BindException exception) {
        final Map<String, String> errorsMap = exception.getBindingResult().getFieldErrors()
                .stream()
                .filter(field -> nonNull(field.getDefaultMessage()))
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return new ResponseEntity<>(new ErrorResponse(errorsMap), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<?> runtimeException(MissingServletRequestParameterException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorResponse {

    @JsonProperty(value = "message", index = 1)
    private String defaultErrorMessage = "bad request";
    @JsonProperty(value = "errors", index = 2)
    private Map<String, String> errors;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", shape = JsonFormat.Shape.STRING)
    @JsonProperty(index = 3)
    private LocalDateTime timestamp = LocalDateTime.now(Clock.systemDefaultZone());

    public ErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public ErrorResponse(String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
    }

}
