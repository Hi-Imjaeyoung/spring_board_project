package com.encore.board.common;

import com.encore.board.author.domain.Author;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private final AuthorService authorService;
    public LoginService(AuthorService authorService){
        this.authorService = authorService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorService.findByEmail(username);
        // 권한이 여러개일 수 있음으로 List로 받음
        List<GrantedAuthority> authorities = new ArrayList<>();
        // ROLE_권한 이 패턴으로 스프링에서 기본적으로 권한 체크
        // 꼭 붙이세용.
        authorities.add(new SimpleGrantedAuthority("ROLE_"+author.getRole()));
        // 매개변수 : userEmail, UserPass, 권한(authorities)
        // TODO : 세션에 저장되는 값!!
        // 해당 메서드에서 return되는 User 객체는 session 메모리 저장소에 저장되어 계속 사용가능
        return new User(author.getEmail(),author.getPassword(),authorities);
    }
}
