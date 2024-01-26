package com.encore.board.post.repository;

import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//    List<Post> findAllByOrderByCreatedTimeDesc();

//    page 넘버(page=1), page마다 게시물 수(size=10), 정렬기준(sort = createdTime,desc) 등이 pageable객체 안에 존재
//    Page객체는 list<Post> + 해당 Page의 각종 정보
    Page<Post> findAll(Pageable pageable);


    Page<Post> findByAppointment(String appointment,Pageable pageable);

    // select p.* from Post p left join author a on p.author_id = a.id
    // 아래 jpql의 join문은 author 객체를 통해 post를 스크리닝 하고 싶은 상황에 적합하다.
    @Query("select p from Post p left join p.author") //jpql문
    List<Post> findAllJoin();

    // select p.*,a.* from Post p left join author a on p.author_id = a.id
    @Query("select p from Post p left join fetch p.author") //jpql문
    List<Post> findAllFetchJoin();

}
