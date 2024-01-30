package com.encore.board.author.controller;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class AuthorController {
    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author/create")
    public String authorCreate(){
        return "author/author-create";
    }

    @PostMapping("/author/create")
    public String authorSave(Model model,AuthorSaveReqDto authorSaveReqDto){
        try {
            authorService.save(authorSaveReqDto);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            model.addAttribute("errorMessage",e.getMessage());
            // TODO: Model을 넣어주기 위해서는 url경로를 줘야한다.
            return "author/author-create";
        }
        return "redirect:/author/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    // ADMIN 유저가 아니면 조회가 불가능 하게 함
    @GetMapping("author/list")
    public String authorList(Model model) {
        model.addAttribute("authorList", authorService.findAll());
        return "author/author-list";
    }

    @GetMapping("author/detail/{id}")
    public String authorDetail(@PathVariable Long id, Model model){
        model.addAttribute("author", authorService.findAuthorDetail(id));
        return "author/author-detail";
    }

    @PostMapping("author/{id}/update")
    public String authorUpdate(@PathVariable Long id, AuthorUpdateReqDto authorUpdateReqDto, Model model) {
        authorService.update(id, authorUpdateReqDto);
        model.addAttribute("author", authorUpdateReqDto);
        return "redirect:/author/detail/" + id;
    }

    @GetMapping("author/delete/{id}")
    public String authorDelete(@PathVariable Long id){
        authorService.delete(id);
        return "redirect:/author/list";
    }

    //Entity 순환참조 이슈 테스트
    @GetMapping("/author/{id}/circle/entity")
    @ResponseBody
    // 연관관계가 있는 author entity를 json으로 직렬화 하게 될 경우 순환참조 이슈 발생하므로, dto 사용필요
    public Author circleIssueTest1(@PathVariable Long id){
        return authorService.findById(id);
    }

    @GetMapping("/author/{id}/circle/dto")
    @ResponseBody
    public AuthorDetailResDto circleIssueTest2(@PathVariable Long id){
        return authorService.findAuthorDetail(id);
    }

    @GetMapping("/author/login-page")
    public String authorLogin(){
        return "author/author-login";
    }
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
