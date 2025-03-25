package com.hackforfun.coronatrackerbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hackforfun.coronatrackerbackend.model.Comment;
import com.hackforfun.coronatrackerbackend.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 모든 댓글 조회
    @GetMapping("/")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    // 댓글 생성 API
    @PostMapping("/create")
    public String createComment(@RequestBody Comment comment) {
        commentService.createComment(comment);  // 댓글 생성
        return "Comment created successfully!";
    }
}
