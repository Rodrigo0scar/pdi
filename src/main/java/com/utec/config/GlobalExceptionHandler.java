package com.utec.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


/*@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errores = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.put(error.getField(), error.getDefaultMessage())
    );

    Map<String, Object> respuesta = new HashMap<>();
    respuesta.put("error", "Error de validación");
    respuesta.put("detalle", errores);

    return ResponseEntity.badRequest().body(respuesta);
}*/

// JSON mal formado o tipos de datos mal
@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<Map<String, String>> handleInvalidFormat(HttpMessageNotReadableException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Error de formato en la solicitud");
    error.put("detalle", ex.getMostSpecificCause().getMessage());
    return ResponseEntity.badRequest().body(error);
}

// Recurso no encontrado
@ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Recurso no encontrado");
    error.put("detalle", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
}

// Errores de negocio
@ExceptionHandler(BadRequestException.class)
public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Solicitud incorrecta");
    error.put("detalle", ex.getMessage());
    return ResponseEntity.badRequest().body(error);
}

// Error genérico inesperado
@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, String>> handleGenericError(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Error interno del servidor");
    error.put("detalle", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
}

    // Errores de validación de DTO
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errores = ex.getConstraintViolations().stream()
                .map(cv -> String.format("Campo '%s': %s",
                        cv.getPropertyPath(),
                        cv.getMessage()))
                .collect(Collectors.toList());

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Error de validación de datos del campo");
        body.put("detalles", errores);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Error de validación");
        body.put("detalles", errores);

        return ResponseEntity.badRequest().body(body);
    }

}
