package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    // 가짜객체를 만드는 작업을 Moking이라 한다
    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void findAuthorDetailTest(){
        //given
        Author author = Author.builder()
                .id(1L)
                .email("naver")
                .name("재영")
                .password("123")
                .posts(new ArrayList<>())
                .build();
        author.getPosts().add(Post.builder().title("제목").contents("내용").author(author).build());
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        //when
        AuthorDetailResDto authorDetailResDto = authorService.findAuthorDetail(1L);
        //then
        Assertions.assertEquals(authorDetailResDto.getPostsNumber(),1);
        Assertions.assertEquals(authorDetailResDto.getRole(),"일반유저");
        Assertions.assertEquals(authorDetailResDto.getName(),"재영");

    }
    @Test
    void update() {
        //given
        Author author = Author.builder()
                .id(1L)
                .email("ss")
                .name("재영")
                .password("123")
                .build();
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        AuthorUpdateReqDto authorUpdateReqDto = new AuthorUpdateReqDto();
        authorUpdateReqDto.setName("변경된 이름");
        authorUpdateReqDto.setPassword("변경된 이메일");
        //when
        //TODO : author를 리턴하지 않아도 된다. 주소를 넘겨주기 떄문에
        Author authorChanged = authorService.update(1L,authorUpdateReqDto);
        //then
        Assertions.assertEquals(authorChanged.getPassword(),authorUpdateReqDto.getPassword());
    }

    @Test
    void findAllTest() {
        //Mock Repo 기능 구현
        List<Author> authors = new ArrayList<>();
        authors.add(Author.builder().id(1L).email("ss").build());
        authors.add(Author.builder().id(1L).email("ss").build());
        authors.add(new Author());
        Mockito.when(authorRepository.findAll()).thenReturn(authors);
        //given

        //when

        //then
        Assertions.assertEquals(3,authorService.findAll().size());
    }
}