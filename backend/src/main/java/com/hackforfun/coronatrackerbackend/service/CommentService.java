package com.hackforfun.coronatrackerbackend.service;

import com.hackforfun.coronatrackerbackend.exception.CommentCollectionException;
import com.hackforfun.coronatrackerbackend.model.Comment;
import com.hackforfun.coronatrackerbackend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void createComment(Comment comment) throws ConstraintViolationException, CommentCollectionException {
        // Optional: Add validation logic
        Optional<Comment> existingComment = commentRepository.findById(comment.getId());
        
        if (existingComment.isPresent()) {
            throw new CommentCollectionException(CommentCollectionException.CommentAlreadyExists());
        }
        
        commentRepository.save(comment);
    }

    public void updateComment(String id, Comment editedComment) throws ConstraintViolationException, CommentCollectionException {
        Optional<Comment> commentToUpdate = commentRepository.findById(id);
        
        if (commentToUpdate.isEmpty()) {
            throw new CommentCollectionException(CommentCollectionException.CommentNotFound());
        }
        
        // Update the existing comment with new values
        Comment existingComment = commentToUpdate.get();
        existingComment.setTitle(editedComment.getTitle());
        existingComment.setDescription(editedComment.getDescription());
        // Add other fields as needed
        
        commentRepository.save(existingComment);
    }

    public void deleteCommentById(String id) throws CommentCollectionException {
        Optional<Comment> commentToDelete = commentRepository.findById(id);
        
        if (commentToDelete.isEmpty()) {
            throw new CommentCollectionException(CommentCollectionException.CommentNotFound());
        }
        
        commentRepository.deleteById(id);
    }
}