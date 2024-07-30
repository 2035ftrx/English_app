package org.example.studyenglishjava.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.studyenglishjava.dto.ApiResponse;
import org.example.studyenglishjava.exception.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Log logger = LogFactory.getLog(this.getClass());

    public static void sendJsonErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        try (PrintWriter writer = new PrintWriter(response.getWriter())) {
            new ObjectMapper().writeValue(writer, new ApiResponse<>(
                    403,
                    false,
                    message,
                    null
            ));
        } catch (IOException ioe) {
            // Handle serialization errors if needed
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> unauthorizedException(UnauthorizedException e) {
        logger.error(" catch FORBIDDEN exception ex= " + e.getMessage());
        e.printStackTrace();
        return new ApiResponse<>(
                0,
                false,
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> notValidException(MethodArgumentNotValidException e) {
        logger.error(" catch MethodArgumentNotValidException exception ex= " + e.getMessage());
        e.printStackTrace();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(";"));
        return new ApiResponse<>(
                0,
                false,
                message,
                null
        );
    }

    /**
     * Default global exception handler.
     *
     * @param e the exception
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> exception(Exception e) {
        logger.error(" catch INTERNAL_SERVER_ERROR exception ex= " + e.getMessage());
        e.printStackTrace();
        return new ApiResponse<>(
                0,
                false,
                e.getMessage(),
                null
        );
    }

    // You can add other custom exception handlers or Spring MVC predefined exception handlers as needed
}