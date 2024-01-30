package com.encore.board.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 해당 어노테이션은 spring security설정을 커스텀하기 위함
// WebSecurityConfigureAdaptor를 상속하는 방식은 deprecated(지원 종료)되었다.
@EnableGlobalMethodSecurity(prePostEnabled = true)
// pre 사전 post 사후,  어떤 작업 사전/사후에 인증/권한 검사 어노테이션 사용 가능
public class SecurityConfig {
    // spring에서 제공하는 기본 filter를 커스텀
    // 로그인 실패시 팅기는 화면, 로그인 시 어떤 로직을 사용할지 등등
    // doLogin 과 doLogout을 기반으로 사용 두 로직이 필터체인안에 들어감
    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // csrf 보안 공격에 대한 설정은 하지 않겠다라는 의미 (rest api 구조에서는 csrf 공격이 구조적으로 힘듬)
                .csrf().disable()
                // 특정 url에 대해서는 보안을 인증처리 하지않고 (로그인 화면, 홈 화면 등등)
                // 특정 url에 대해서는 인증처리 하겠다. (인증 처리 = 로그인)
                .authorizeRequests()
                    // 인증 미적용 url 패턴
                    .antMatchers("/","/author/create","/author/login-page")
                        .permitAll()
                    // 그외 요청은 모두 인증이 필요.
                    .anyRequest().authenticated()
                .and()
                // 만약에 셰션방식의 로그인을 사용하지 않으면 아래 내용 설정한다.
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .formLogin()
                    .loginPage("/author/login-page")
                    // 스프링 내장 메서드를 사용하기 위해 /doLogin url 사용
                    .loginProcessingUrl("/doLogin")
                        //키 값
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                    .successHandler(new LoginSuccessHandler())
                .and()
                .logout()
                    // spring security의 doLogout 기능 그대로 사용
                    .logoutUrl("/doLogout")
                .and()
                .build();
    }
}
