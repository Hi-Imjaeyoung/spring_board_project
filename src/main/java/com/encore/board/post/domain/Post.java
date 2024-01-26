package com.encore.board.post.domain;

import com.encore.board.author.domain.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 3000)
    private String contents;
    @Column
    private String appointment;
    @Column
    LocalDateTime appointTime;
    // author_id는 DB의 컬럼명, 별다른 옵션이 없을 시 author의 pk에 fk가 설정
    // Post 객체를 조회 할때, Author를 가져온다.
    // post 객체 입장에서는 한 사람이 여러개 글을 쓸 수 있으므로 N:1이다
    @ManyToOne(fetch = FetchType.LAZY) // 왜 알려줘야할까
    @JoinColumn(name = "author_id")
//    @JoinColumn(nullable = false, name="author_email",referencedColumnName = "email")
    private Author author;

    @CreationTimestamp
    // 개발자가 DB를 바꾸는 게 risky한 것. 프로그램적으로 다루는 것이 좋다.
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;


    @Builder
    public Post(String title, String contents,Author author,String appointment,LocalDateTime appointTime){
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.appointment = appointment;
        this.appointTime = appointTime;
//        author 객체의 posts를 초기화 시켜준 후
//        this.author.getPosts().add(this);
    }
    public void updatePost(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
    public void updateAppointment(){
        this.appointment = null;
    }
//    TODO : 될까 안될까요?
//    public void addPost(){
//        this.author.getPosts().add(this);
//    }
}
