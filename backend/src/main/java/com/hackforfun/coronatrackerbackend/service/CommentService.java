package com.hackforfun.coronatrackerbackend.service;

import com.hackforfun.coronatrackerbackend.model.Comment;
import com.hackforfun.coronatrackerbackend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> updateComment(String id, Comment updatedComment) {
        return commentRepository.findById(id).map(existingComment -> {
            existingComment.setTitle(updatedComment.getTitle());
            existingComment.setDesc(updatedComment.getDesc());
            return commentRepository.save(existingComment);
        });
    }

    public boolean deleteComment(String id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}