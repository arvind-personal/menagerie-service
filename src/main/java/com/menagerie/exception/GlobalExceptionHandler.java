package com.menagerie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<ProblemDetail> handleNotFound(NotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        return Mono.just(problem);
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ProblemDetail> handleBadRequest(BadRequestException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Bad Request");
        return Mono.just(problem);
    }

   // @ExceptionHandler(Exception.class)
    public Mono<ProblemDetail> handleAll(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.");
        problem.setTitle("Internal Server Error");
        return Mono.just(problem);
    }
}
