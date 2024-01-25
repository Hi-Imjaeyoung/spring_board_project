package com.encore.board.author.controller;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

//WebMvcTest를 이용해서 Controller계층을 테스트. 모든 스프링빈을 생성하고 주입하지는 않음.
@WebMvcTest(AuthorController.class)

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

//    @Test
////    @WithMockUser// security 의존성 추가 필요
//    void authorDetailTest() throws Exception{
//        AuthorDetailResDto author = new AuthorDetailResDto();
//        author.setName("신테스트");
//        author.setEmail("신@네이버");
//        author.setPassword("신라면");
//        Mockito.when(authorService.findAuthorDetail(1L)).thenReturn(author);
//        mockMvc.perform(MockMvcRequestBuilders.get("/author/1/circle/dto"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.name",author.getName()));
//    }

}