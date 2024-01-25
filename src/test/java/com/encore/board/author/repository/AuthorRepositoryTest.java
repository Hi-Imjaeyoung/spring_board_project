package com.encore.board.author.repository;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//SpringBootTest 어노테이션은 자동 롤백기능은 지원하지 않고, 별도로 롤백 코드 또는 어노테이션 필요,
//모든 Bean을 사용(실제 스프링 프로젝트와 동일) 빌드 속도가 느림
//@SpringBootTest
//@Transactional

//DataJpaTest 어노테이션을 사용하면 매 테스트가 종룓히면 자동으로 DB 원상 복구
//모든 SpringBean을 생성하지 않고, DB 특화 어노테이션
@DataJpaTest

//replace = AutoConfigureTestDatabase.Replace.ANY (디폴트 설정) : H2DB(spring 내장 인메모리)가 기본 설정
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// application-test.yml 파일을 찾아 설정값을 세팅
@ActiveProfiles("test")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void authorSaveTest(){
        // 객체 생성 -> sava -> 재조회 -> 비교

        // 준비(given)
        Author author = Author.builder()
                              .name("임테스트")
                              .email("임테스트@이메일")
                              .password("123")
                              .role(Role.ADMIN)
                              .build();
        // 실행(when)
        authorRepository.save(author);
        Author authorDB = authorRepository.findByEmail("임테스트@이메일").orElse(null);
        // 검증(then)
        // Assertions클래스의 기능을 통해 오류의 원인파악, null처리, 가시적으로 성공/실패 여부 확인
        Assertions.assertEquals(author.getEmail(),authorDB.getEmail());
    }


}