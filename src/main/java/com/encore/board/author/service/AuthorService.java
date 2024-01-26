package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto) throws IllegalArgumentException{
        if(authorRepository.findByEmail(authorSaveReqDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이메일이 중복입니다");
        }
        Role role = null;
        if(authorSaveReqDto.getRole() == null || authorSaveReqDto.getRole().equals("user")){
            role = Role.USER;
        }else{
            role = Role.ADMIN;
        }
        //일반 생성자 방식
        //Author author = new Author(authorSaveReqDto.getName(), authorSaveReqDto.getEmail(), authorSaveReqDto.getPassword(), role);

        //빌더패턴
        // .build() : 최종적으로 완성시키는 단계
        Author author = Author.builder()
                .email(authorSaveReqDto.getEmail())
                .name(authorSaveReqDto.getName())
                .password(authorSaveReqDto.getPassword())
                .role(role)
                .build();

        // cascade.persist 테스트
        // 부모테이블을 통해 자식테이블에 객체를 동시에 생성
//        List<Post> posts = new ArrayList<>();
//        Post post = Post.builder()
//                .title("안녕하세요 저는 "+author.getName()+" 입니다.")
//                .contents("반갑습니다. cascade test 중")
//                .author(author)
//                .build();
//        posts.add(post);
//        author.setPosts(posts);
        authorRepository.save(author);
    }

    public List<AuthorListResDto> findAll() {
        List<Author> Authors = authorRepository.findAll();
        List<AuthorListResDto> AuthorListResDtos = new ArrayList<>();
        for(Author author : Authors){
            AuthorListResDto authorListResDto = new AuthorListResDto();
            authorListResDto.setId(author.getId());
            authorListResDto.setName(author.getName());
            authorListResDto.setEmail(author.getEmail());
            AuthorListResDtos.add(authorListResDto);
        }
        return AuthorListResDtos;
//        return authorRepository.findAll().stream().map(author -> new AuthorListResDto(author.getId(), author.getName(), author.getEmail())).toList();
    }

    public Author findById(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 Member가 없습니다."));
        return author;
    }

    public AuthorDetailResDto findAuthorDetail(Long id) throws EntityNotFoundException {
        Author author = this.findById(id);
        String role = null;
        if(author.getRole() == null || author.getRole().equals(Role.USER)){
            role = "일반유저";
        }else{
            role = "관리자";
        }
        //TODO : detailDto 에서는 list를 전체를 가져야 할까? 사이즈만 가져도 될까?
        AuthorDetailResDto authorDetailResDto = AuthorDetailResDto.builder()
                .id(author.getId())
                .name(author.getName())
                .password(author.getPassword())
                .role(role)
                .email(author.getEmail())
                .createdTime(author.getCreatedTime())
                .postsNumber(author.getPosts().size())
                .build();
//        authorDetailResDto.setId(author.getId());
//        authorDetailResDto.setName(author.getName());
//        authorDetailResDto.setEmail(author.getEmail());
//        authorDetailResDto.setPassword(author.getPassword());
//        authorDetailResDto.setRole(role);
//        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        return authorDetailResDto;
    }

    public Author update(Long id, AuthorUpdateReqDto authorUpdateReqDto) throws EntityNotFoundException {
        Author author = this.findById(id);
        author.updateAuthor(authorUpdateReqDto.getName(), authorUpdateReqDto.getPassword());
        // TODO : 더티쳌 , Transactional 이 필요함
        // 명시적으로 save 하지 않더라도 jpa의 영속성 컨텍스트를 통해 객체의 변경이 감지 되면(더티 체킹) Transaction이 완료되는 시점에
        // save가 동작.
        authorRepository.save(author);
        return author;
    }

    public void delete(Long id) throws EntityNotFoundException {
        Author author = this.findById(id);
        authorRepository.delete(author);
//        authorRepository.deleteById(id);
    }
}


