package com.promemory.global.exception;

import static com.promemory.global.exception.type.ErrorCode.ACCESS_DENIED;

import com.promemory.global.exception.dto.ErrorResponse;
import com.promemory.global.exception.type.ErrorCode;
import io.jsonwebtoken.MalformedJwtException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ErrorResponse customExceptionHandler(CustomException e) {
        log.error("'{}':'{}'", e.getErrorCode(), e.getErrorCode().getMessage());
        return new ErrorResponse(e.getErrorCode(), e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return new ErrorResponse(ErrorCode.VALIDATION_FAILED,
                ErrorCode.VALIDATION_FAILED.getStatus().value(), errors.toString());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException is occurred", e);

        return new ErrorResponse(ACCESS_DENIED
                , ACCESS_DENIED.getStatus().value(), ACCESS_DENIED.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ErrorResponse handleMalformedJwtException(MalformedJwtException e) {
        log.error("MalformedJwtException is occurred", e);

        return new ErrorResponse(ACCESS_DENIED
                , ACCESS_DENIED.getStatus().value(), ACCESS_DENIED.getMessage());
    }


}
