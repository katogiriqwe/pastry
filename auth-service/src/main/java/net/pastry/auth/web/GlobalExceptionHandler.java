package net.pastry.auth.web;

import net.pastry.auth.account.exception.UsernameAlreadyTakenException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private record FieldViolation(String pointer, String detail, String code) {}

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ProblemDetail> handleAuthentication() {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication required");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
                .body(problem);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail handleAccessDenied() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    ProblemDetail handleUsernameAlreadyTaken(UsernameAlreadyTakenException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid username or password" );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new FieldViolation("#/" + e.getField(), e.getDefaultMessage(), e.getCode()))
                .toList();

        var problem = ProblemDetail.forStatusAndDetail(status, "Validation failed");
        problem.setProperty("errors", errors);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }
}
