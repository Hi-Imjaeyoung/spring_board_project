package com.encore.board.post.service;

import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostCreateReqDto;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    // TODO : AuthorService를 주입 받으면 좀 더 유연한 코드가 될수 있는데.. 순환참조 이슈가 생길 수 있다 .
    private final AuthorRepository authorRepository;
    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateReqDto postSaveReqDto){
//        Post post = new Post(postCreateReqDto.getTitle(), postCreateReqDto.getContents());
        String appoint =null;
        Post post = null;
        LocalDateTime localDateTime=null;
        // Y를 선택하고 시간을 선택하지 않았으면 그냥 N으로 처리
        if(postSaveReqDto.isAppointment() && !postSaveReqDto.getAppointTime().isEmpty()){
            appoint ="Y";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            localDateTime = LocalDateTime.parse(postSaveReqDto.getAppointTime(),dateTimeFormatter);
            LocalDateTime now = LocalDateTime.now();
            if(localDateTime.isBefore(now)){
                throw new IllegalArgumentException("현재 시간보다 이른 시간으로 예약할수 없습니다.");
            }
        }
        post = Post.builder()
                .title(postSaveReqDto.getTitle())
                .contents(postSaveReqDto.getContents())
                .author(authorRepository.findByEmail(postSaveReqDto.getEmail()).orElse(null))
                .appointment(appoint)
                .appointTime(localDateTime)
                .build();
        postRepository.save(post);
    }

    public List<PostListResDto> findAll(){
        List<Post> Posts = postRepository.findAll();
        List<PostListResDto> PostListResDtos = new ArrayList<>();
        for(Post post : Posts){
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());
            //TODO : 삼항 연산자를 이용한 null값 처리
            postListResDto.setAuthor_email(post.getAuthor()==null?"익명 유저":post.getAuthor().getEmail());
            PostListResDtos.add(postListResDto);
        }
        return PostListResDtos;
    }

    public Post FindById(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 Post가 없습니다."));
        return post;
    }

    public PostDetailResDto findPostDetail(Long id) throws EntityNotFoundException {
        Post post = this.FindById(id);
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setId(post.getId());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setContents(post.getContents());
        postDetailResDto.setCreatedTime(post.getCreatedTime());
        return postDetailResDto;
    }

    public void update(Long id, PostUpdateReqDto postUpdateReqDto){
        Post post = this.FindById(id);
        post.updatePost(postUpdateReqDto.getTitle(), postUpdateReqDto.getContents());
        postRepository.save(post);
    }

    public void delete(Long id){
        Post post = this.FindById(id);
        postRepository.delete(post);
    }

//    public Page<PostListResDto> findAllPaging(Pageable pageable){
//        Page<Post> posts = postRepository.findAll(pageable);
//        Page<PostListResDto> PostListResDtos
//                = posts.map(p->new PostListResDto(p.getId()
//                ,p.getTitle()
//                ,p.getAuthor()==null?"익명 유저":p.getAuthor().getEmail()));
//        // Page 객체 변환이 쉽지않다
//        return PostListResDtos;
//    }

    public Page<PostListResDto> findByAppointmentPaging(Pageable pageable){
        Page<Post> posts = postRepository.findByAppointment(null,pageable);
        Page<PostListResDto> PostListResDtos
                = posts.map(p->new PostListResDto(p.getId()
                ,p.getTitle()
                ,p.getAuthor()==null?"익명 유저":p.getAuthor().getEmail()));
        // Page 객체 변환이 쉽지않다
        return PostListResDtos;
    }
}
