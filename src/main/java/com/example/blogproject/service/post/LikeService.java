package com.example.blogproject.service.post;

import com.example.blogproject.entity.post.Like;
import com.example.blogproject.entity.post.Post;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.filter.UserContextHolder;
import com.example.blogproject.repository.post.LikeRepository;
import com.example.blogproject.repository.post.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public ResponseEntity<Map<String, Object>> postLike(HttpServletRequest request, Long post_id){
        // 현재 로그인 중인 유저 검색
        User user = UserContextHolder.getUser();

        // 좋아요할 게시글 조회
        Post like_post = postRepository.findById(post_id).orElse(null);

        // 좋아요가 되었는지 좋아요가 취소되었는지 알기위한 알림 문구
        String notice = "";
        boolean liked = false;

        // 좋아요할 게시글에 이미 좋아요 처리가 되어있는지 확인
        if(likeRepository.findByUserIdAndPostId(user.getId(), post_id) != null){
            // 좋아요가 이미 되어있을 경우 MemberLikePost 에서 좋아요 삭제처리
            Like unLikeBoard = likeRepository.findByUserIdAndPostId(user.getId(), post_id);
            likeRepository.delete(unLikeBoard);

            // 좋아요 취소 시 notice에 문구 반영
            notice = "좋아요 취소";
        }else{
            // 유저가 게시글에 좋아요한 이력 저장
            Like likePost = Like.builder()
                    .post(like_post)
                    .user(user)
                    .build();

            likeRepository.save(likePost);

            // 좋아요 시 notice에 문구 반영
            notice = "좋아요";
            liked = true;
        }

        // 해당 게시글좋아요 수
        Long likeCnt = likeRepository.countByPostId(post_id);

        // 게시글 likes 정보 업데이트
        postRepository.updateLikes(post_id, likeCnt);

        Map<String, Object> response = new HashMap<>();
        response.put("notice", notice);
        response.put("liked", liked);

        return ResponseEntity.ok(response);
    }
}