package com.encore.board.author.controller;

import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("userinfo/test")
    public String userInfoTest(HttpServletRequest request){
        //로그인 유저 정보 얻는 방식
        // 방법 1. Session에 attribute를 통해 접근
        String email1 = (String) request.getSession().getAttribute("email");
        System.out.println(email1);
        // 방법 2. Session에서 Authentication 객체를 접근
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        String email2 = securityContext.getAuthentication().getName();
        System.out.println(email2);
        // 방법 3. SecurityContextHolder에서 Authentication 객체를 접근 (가장 일반적)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email3 = authentication.getName();
        System.out.println(email3);
        return null;
    }
}
