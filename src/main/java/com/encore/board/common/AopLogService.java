package com.encore.board.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class AopLogService {

    //  aop의 대상이 되는 controller 서비스 등을 정의하는 어노테이션
//    @Pointcut("excution(* com.encore.board..controller..*.*(..))")

    // 어노테이션을 대상으로 잡음
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointcut(){

    }

//     방식 1. 비포 에프터 사용.
//    @Before("controllerPointcut()")
//    public void beforeController(JoinPoint joinPoint){
//        log.info("Before Controller");
//        ServletRequestAttributes servletRequestAttributes= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
//        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
//        // Json 형태로 사용자의 요청을 조립하기 위한 로직
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode objectNode = objectMapper.createObjectNode();
//        objectNode.put("Method Name",joinPoint.getSignature().getName());
//        objectNode.put("CRUD Name",httpServletRequest.getMethod());
//        Map<String,String[]> paramMap =httpServletRequest.getParameterMap();
//        ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
//        objectNode.set("user Inputs",objectNodeDetail);
//        log.info("user request info"+ objectNode);
//    }
//    @After("controllerPointcut()")
//    public void afterController(){
//        log.info("End Controller");
//    }


    // 방식 2. around 사용 (가장 빈번하게 사용)
    @Around("controllerPointcut()")
    // joinpoint란 aop대상으로 하는 컨트롤러의 특정 메서드를 의미
    public Object controllerLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        //log.info("request method"+proceedingJoinPoint.getSignature().toString());

        // 사용자의 요청 값을 출력하기 위해 httpservletRequest객체를 꺼내는 로직
        ServletRequestAttributes servletRequestAttributes= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        // Json 형태로 사용자의 요청을 조립하기 위한 로직
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("Method Name",proceedingJoinPoint.getSignature().getName());
        objectNode.put("CRUD Name",httpServletRequest.getMethod());
        Map<String,String[]> paramMap =httpServletRequest.getParameterMap();
        ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
        objectNode.set("user Inputs",objectNodeDetail);
        log.info("user request info"+ objectNode);

        try {
            // 본래의 컨트롤러 메서드 호출하는 부분
            return proceedingJoinPoint.proceed();
        }catch (Throwable e){
            log.error(e.getMessage());
            return new RuntimeException(e);
        }finally {
            log.info("end controller");
        }
    }
}
