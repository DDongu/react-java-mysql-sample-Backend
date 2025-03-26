package com.hackforfun.coronatrackerbackend.controller;

import com.hackforfun.coronatrackerbackend.model.Comment;
import com.hackforfun.coronatrackerbackend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

	@GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Application is healthy");
    }

    @GetMapping("/getComments")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @PostMapping("/saveComments")
    public Comment saveComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @PutMapping("/updateComment/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody Comment updatedComment) {
        Optional<Comment> result = commentService.updateComment(id, updatedComment);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        return commentService.deleteComment(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}