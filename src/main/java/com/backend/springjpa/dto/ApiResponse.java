package com.backend.springjpa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class ApiResponse<T> {

    private String status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private String timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    public ApiResponse() {}

    public ApiResponse(String status, String message, T data, String path) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
        this.path = path;
    }

    // Factory method untuk success tanpa request
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>("Success", "success", data, null);
    }

    public static <T> ApiResponse<T> ok(String message, T data, String path) {
        return new ApiResponse<>("Success", message, data, path);
    }

    // Factory method untuk success dengan request
    public static <T> ApiResponse<T> ok(String message, T data, HttpServletRequest request) {
        return new ApiResponse<>("Success", message, data, request.getRequestURI());
    }

    // Factory method untuk fail
    public static <T> ApiResponse<T> fail(String message, HttpServletRequest request) {
        return new ApiResponse<>("Error", message, null, request.getRequestURI());
    }

    // Getters & Setters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getTimestamp() { return timestamp; }
    public String getPath() { return path; }
    public void setStatus(String status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setPath(String path) { this.path = path; }
}

