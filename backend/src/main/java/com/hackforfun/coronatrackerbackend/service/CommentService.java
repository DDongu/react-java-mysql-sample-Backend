package com.hackforfun.coronatrackerbackend.service;

import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackforfun.coronatrackerbackend.exception.CommentCollectionException;
import com.hackforfun.coronatrackerbackend.model.Comment;
import com.hackforfun.coronatrackerbackend.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepo;

	public List<Comment> getAllComments() {
		List<Comment> comments = commentRepo.findAll();
		if (comments.size() > 0) {
			return comments;
		} else {
			return new ArrayList<Comment>();
		}
	}

	public void createComment(Comment comment)
			throws ConstraintViolationException, CommentCollectionException {

		// If the comment is valid as per not null constraint we have to next
		// check if the comment with the same name/id already exists
		Optional<Comment> commentTitleOptional = commentRepo
				.findByTitle(comment.getTitle());
		if (commentTitleOptional.isPresent()) {
			System.out.println(commentTitleOptional.get());
			throw new CommentCollectionException(
					CommentCollectionException.TitleAlreadyExists());
		} else {
			commentRepo.save(comment);
		}

	}

	public void updateComment(String id, Comment newComment)
			throws ConstraintViolationException, CommentCollectionException {
		
		Comment existingComment = commentRepo.findById(id)
			.orElseThrow(() -> new CommentCollectionException(CommentCollectionException.NotFoundException(id)));

		commentRepo.findByTitle(newComment.getTitle())
			.filter(comment -> !comment.getId().equals(id)) // 같은 ID가 아닐 경우 중복 체크
			.ifPresent(comment -> {
				throw new RuntimeException(new CommentCollectionException(CommentCollectionException.TitleAlreadyExists()));
			});

		existingComment.setTitle(newComment.getTitle());
		existingComment.setDesc(newComment.getDesc());

		commentRepo.save(existingComment);
	}
	
	
	
	public void deleteCommentById(String id) throws CommentCollectionException
    {
        Optional<Comment> commentOptional=commentRepo.findById(id);
        if(!commentOptional.isPresent())
        {
            throw new CommentCollectionException(CommentCollectionException.NotFoundException(id));
        }
        else
        {
        	commentRepo.deleteById(id);
        }
    }

}
