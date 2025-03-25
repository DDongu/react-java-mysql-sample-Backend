package com.hackforfun.coronatrackerbackend.repository;

import com.hackforfun.coronatrackerbackend.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    // Additional custom query methods can be added here if needed
}