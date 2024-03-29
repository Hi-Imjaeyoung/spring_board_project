package com.encore.board.post.controller;

import com.encore.board.post.dto.PostCreateReqDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;

@Controller
@Slf4j
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("post/create")
    public String postCreate() {
        return "post/post-create";
    }
    @PostMapping("post/create")
    public String postSave(Model model, PostCreateReqDto postCreateReqDto,HttpSession session){
       try {
           // 1. 서블릿 리퀘를 매개변수로 받아서 활용
//            HttpServletRequest req를 매개 변수에 주입한 뒤,
//            HttpSession session = req.getSession(); //세션값을 꺼내어 getAttribute("email")한다

           // 2.  Session을 바로 매개변수로 받아서 활용

           // 위 두방법은 LoginSuccessHandler에서 session에 값을 담아줬기 때문에 가능한 것!!

            postService.save(postCreateReqDto,session.getAttribute("email").toString());
            return "redirect:/post/list";
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            model.addAttribute("errorMessage",e.getMessage());
            return "post/post-create";
        }
    }

    @GetMapping("post/list")
    public String postList(Model model,@PageableDefault(size = 5,sort = "createdTime",direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("postList", postService.findByAppointmentPaging(pageable));
        return "post/post-list";
    }

    @GetMapping("post/detail/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findPostDetail(id));
        return "post/post-detail";
//        return postService.findPostDetail(id);
    }

    @PostMapping("post/{id}/update")
    public String postUpdate(@PathVariable Long id, PostUpdateReqDto postUpdateReqDto, Model model) {
        postService.update(id, postUpdateReqDto);
        model.addAttribute("post", postUpdateReqDto);
        return "redirect:/post/detail/" + id;
    }

    @GetMapping("post/delete/{id}")
    public String postDelete(@PathVariable Long id){
        postService.delete(id);
        return "redirect:/post/list";
    }

//    @GetMapping("json/post/list")
//    @ResponseBody
//    // localhost:8080/json/post/list?size=5&page=1&sort=xx,desc
//    public Page<PostListResDto> postList(Pageable pageable) {
//        return postService.findAllJson(pageable);
////        return postService.findAll();
//    }
}