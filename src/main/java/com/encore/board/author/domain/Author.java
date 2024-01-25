package com.encore.board.author.domain;

import com.encore.board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
//TODO : 생성자가 존재하면 기본 생성자 필요
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Author {
    //persistence package
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 20)
    private String name;

    @Column(nullable = false,length = 20,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    //TODO: cascade test
//    @Setter
    // Author를 조회 할때, post객체가 필요할 시 선언
    // Post의 Author의 변수를 명시
    // mappedBy를 연관관계의 주인을 명시하고, fk를 관리하는 변수명을 명시한다.
                                                                // 로딩 전략 디폴트가 LAZY
    //1대1관계인 경우 OneToOne
    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts;

    @CreationTimestamp
    // 개발자가 DB를 바꾸는 게 risky한 것. 프로그램적으로 다루는 것이 좋다.
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;

//     @Builder // 클래스 단에 붙여주지 않으면 메서드 단에서 설정 가능
//    public Author(String name, String email, String password, Role role){
//        this.name = name;
//        this.email = email;
//        this.pwd = password;
//        this.role = role;
        //TODO:setter를 안쓰기
//        posts = new ArrayList<>();
//        Post post = Post.builder()
//                .title("안녕하세요 저는 "+this.name+" 입니다.")
//                .contents("반갑습니다. cascade test 중")
//                .author(this)
//                .build();
//        posts.add(post);
//    }

    public void updateAuthor(String name, String password){
        this.name = name;
        this.password = password;
    }
}
