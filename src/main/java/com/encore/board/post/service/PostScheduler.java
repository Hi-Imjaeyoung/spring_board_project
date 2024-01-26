package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class PostScheduler {

    private final PostRepository postRepository;

    @Autowired
    public PostScheduler(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    // 초 분 시간 일 월 요일 형태로 스케줄링 설정
    // *: 매 초(분/시 등)을 의미한다
    // 특정숫자: 특정숫자의 초(분/시)를 의미
    // 0/특정숫자 : 특정숫자 마다
    // ex)  0 0 * * * * : 매일 0분 0초에 스케줄링 시작
    // ex2) 0 0/1 * * * * : 매일 1분마다 0초에 스케줄링 시작
    // ex3) 0/1 * * * * * : 매초마다
    // ex4) 0 0 11 * * * * : 매일 11시에 스케줄링
//    @Scheduled(cron = "0 0/1 * * * *")
//    @Transactional
//    public void postSchedule(){
//        System.out.println("=====스케줄러 시작=====");
//        Page<Post> posts = postRepository.findByAppointment("Y", Pageable.unpaged());
//        for(Post p:posts.getContent()){
//            System.out.println(p.getTitle());
//            if(LocalDateTime.now().isAfter(p.getAppointTime())){
//                //TODO: 더티체킹이 왜 안될까? @Transactional을 붙여줘야함? 응
//                p.updateAppointment();
//                //postRepository.save(p);
//            }
//        }
//        System.out.println("=====스케줄러 끝=====");
//    }
}
