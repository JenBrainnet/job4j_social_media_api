package ru.job4j.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void catchDataIntegrityViolationException(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Map<String, String> details = new HashMap<>();
        details.put("message", exception.getMessage());
        details.put("type", exception.getClass().getName());
        details.put("timestamp", OffsetDateTime.now(ZoneOffset.UTC).toString());
        details.put("path", request.getRequestURI());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(details));
        log.error(exception.getLocalizedMessage());
    }

}
