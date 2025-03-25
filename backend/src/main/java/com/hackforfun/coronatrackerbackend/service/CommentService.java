package com.hackforfun.coronatrackerbackend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hackforfun.coronatrackerbackend.model.Comment;
import com.hackforfun.coronatrackerbackend.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // 모든 댓글 조회
    public List<Comment> getAllComments() {
        return commentRepository.findAll(); // MongoDB에서 모든 댓글 조회
    }

    // 댓글 생성
    public void createComment(Comment comment) {
        commentRepository.save(comment); // MongoDB에 댓글 저장 (컬렉션 자동 생성)
    }
}
