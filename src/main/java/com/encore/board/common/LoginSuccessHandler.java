package com.encore.board.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 로그인이 성공했을떄 실행할 로직 정의
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    // Authentication User 객체가 담겨 있는 곳
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession httpSession = request.getSession();
        // authentication 객체 안에는 user 객체가 들어가 있고, getName은 이메일을 의미한다
        // 전역적으로 사용하기 위해 HttpSession에 넣어줬다
        // 아래 로직이 없어서 spring에서 authentic으로 접근이 가능하다. 다만 타임리프에서 Session으로 접근하기
        // 위해서 담아줬다. (Session 객체가 접근성이 높다)
        httpSession.setAttribute("email",authentication.getName());
        response.sendRedirect("/");
    }
}
