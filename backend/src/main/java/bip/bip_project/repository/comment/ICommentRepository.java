package bip.bip_project.repository.comment;

import bip.bip_project.model.comment.Comment;
import bip.bip_project.model.comment.CommentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByReviewId(Integer reviewId);
}
