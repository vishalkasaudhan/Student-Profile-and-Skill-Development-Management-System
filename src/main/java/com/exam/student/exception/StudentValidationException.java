package com.exam.student.exception;

public class StudentValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StudentValidationException(String message) {
        super(message);
    }
}
