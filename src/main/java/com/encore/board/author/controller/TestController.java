package com.encore.board.author.controller;

import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Lombok 내 slf4j 어노테이션을 통해 쉽게 logback 라이브러리 사용가능
@Slf4j
public class TestController {
    // slf4j 임포트 하지않고 직접 라이브러리 import하여 로거 생성가능
//     private static final Logger loger = LoggerFactory.getLogger(LogTestController.class);
    @Autowired
    private AuthorService authorService;

    @GetMapping("log/test")
    public String testMethod1(){
        log.debug("debug log");
        log.info("info log");
        log.error("error log");
        return "OK";
    }

    @GetMapping("exception/test/{id}")
    public String exceptionTest(@PathVariable Long id){
        authorService.findById(id);
        return "OK";
    }
}
