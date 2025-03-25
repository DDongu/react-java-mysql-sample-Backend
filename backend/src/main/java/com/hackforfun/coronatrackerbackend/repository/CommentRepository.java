package com.hackforfun.coronatrackerbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.hackforfun.coronatrackerbackend.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
    
    // ✅ 제목이 정확히 일치하는 댓글 찾기
    @Query("{'title': ?0}")
    Optional<Comment> findByTitle(String title);

    // ✅ 제목이 대소문자 무시하고 일치하는 댓글 찾기 (부분 검색 가능)
    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    List<Comment> findByTitleIgnoreCase(String title);

    // ✅ 가장 최근에 생성된 댓글 찾기 (MongoDB의 `_id`는 ObjectId이며, 생성 시간이 포함됨)
    @Query(value = "{}", sort = "{'_id': -1}")
    Optional<Comment> findLatestComment();
}
