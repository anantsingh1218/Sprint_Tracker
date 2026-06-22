package com.sprint.SprintLite.exception;

import com.sprint.SprintLite.dto.ErrorResponseDto;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Tracer tracer;

    @ExceptionHandler(Exception.class) // Handle All Exceptions
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception, WebRequest webRequest){
        TraceContext context = tracer.currentTraceContext().context();
        String traceId = "";
        if (context != null) {
            traceId = context.traceId();
        }
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(), LocalDateTime.now(), traceId
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Handle All Exceptions
    public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();
        fieldErrorList.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class) // Handle All Exceptions
    public ResponseEntity<Map<String, String>> handleException(HandlerMethodValidationException exception){
        Map<String, String> errors = new HashMap<>();
        List<ParameterValidationResult> results = exception.getParameterValidationResults();
        results.forEach(result -> {
            String paramName = result.getMethodParameter().getParameterName();
            // Combine all the params into a single comma separated string
            String combinedMessage = result.getResolvableErrors()
                    .stream()
                    .map(MessageSourceResolvable::getDefaultMessage) // same as error -> error.getDefaultMessage()
                    .collect(Collectors.joining(","));
            errors.put(paramName, combinedMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NullPointerException.class) // Execute in scenarios of NullPointerException only
    public ResponseEntity<ErrorResponseDto> handleNullException(Exception exception, WebRequest webRequest){
        TraceContext context = tracer.currentTraceContext().context();
        String traceId = "";
        if (context != null) {
            traceId = context.traceId();
        }
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR,
                "A Null Pointer Exception due to : " + exception.getMessage(), LocalDateTime.now(),
                traceId
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RegistrationValidationException.class) // Execute in scenarios of NullPointerException only
    public ResponseEntity<Map<String, String>> handleRegistrationException(RegistrationValidationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getErrors());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Map<String, String>> handleFileStorageException(
            FileStorageException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}

