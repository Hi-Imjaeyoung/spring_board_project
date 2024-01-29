package com.encore.board.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

//ControllerAdvice와 ExceptionHandler를 통해 공통화 로직 작성
@ControllerAdvice
@Slf4j
public class CommonException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Object>> entityNotFoundHandler(EntityNotFoundException e){
        log.error("Handler EntityNotFundException message : " + e.getMessage());
        return this.errorResponseMessage(HttpStatus.NOT_FOUND,e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> entityNotFoundHandler(IllegalArgumentException e){
        log.error("Handler IllegalArgumentException message : " + e.getMessage());
        return this.errorResponseMessage(HttpStatus.BAD_REQUEST,e.getMessage());
    }

    private ResponseEntity<Map<String,Object>> errorResponseMessage(HttpStatus status, String message){
        Map<String,Object> body = new HashMap<>();
        body.put("status",String.valueOf(status.value()));
        body.put("error message",message);
        //본문 // 헤더
        return new ResponseEntity<>(body,status);
    }
}
